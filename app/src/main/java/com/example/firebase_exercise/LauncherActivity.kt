package com.example.firebase_exercise

import android.os.Bundle
import com.example.firebase_exercise.common.SharedPrefUtil
import com.example.firebase_exercise.movie_viewer.MovieViewerActivity
import com.example.firebase_exercise.sign_in.SignInActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class LauncherActivity : BaseActivity() {

    @Inject
    lateinit var sharedPrefUtil: SharedPrefUtil

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth.currentUser?.let {
            sharedPrefUtil.setUser(it.email!!)
            launchActivity(MovieViewerActivity::class)
        } ?: run {
            launchActivity(SignInActivity::class)
        }
        finish()
    }

    override fun getDisposable(): CompositeDisposable =
        CompositeDisposable()
}