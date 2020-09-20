package com.example.firebase_exercise

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.example.firebase_exercise.databinding.ActivityMainBinding
import com.example.firebase_exercise.movie_viewer.MovieViewerActivity
import com.example.firebase_exercise.sign_in.SignInActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var sharedPrefUtil: SharedPrefUtil

    private lateinit var firebaseAuth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this
    }

    override fun onResume() {
        super.onResume()
        authenticateUser()
    }

    override fun getDisposable(): CompositeDisposable =
        CompositeDisposable()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.sign_out_menu) {
            firebaseAuth.signOut()
            googleSignInClient.signOut()
            sharedPrefUtil.setUser(SharedPrefUtil.ANONYMOUS)
            launchActivity(SignInActivity::class)
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    fun launchMovieDatabase() {
        launchActivity(MovieViewerActivity::class)
        finish()
    }

    private fun authenticateUser() {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser

        firebaseUser?.let {
            sharedPrefUtil.setUser(it.email!!)
        } ?: run{
            launchActivity(SignInActivity::class)
        }
    }
}