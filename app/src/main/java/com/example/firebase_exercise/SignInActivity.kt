package com.example.firebase_exercise

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebase_exercise.common.FirebaseManager
import com.example.firebase_exercise.common.Toaster
import com.example.firebase_exercise.databinding.ActivitySignInBinding
import com.example.firebase_exercise.movie_viewer.MovieViewerActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class SignInActivity : BaseActivity() {

    @Inject
    lateinit var firebaseManager: FirebaseManager

    @Inject
    lateinit var toaster: Toaster

    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySignInBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        binding.activity = this
    }

    fun signInWithGoogle() {
        startActivityForResult(firebaseManager.googleSignInClient.signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && data != null) {
            firebaseManager.signInToFirebaseWithGoogle(data)
                .subscribeBy(
                    onComplete = { launchMovieViewer() },
                    onError = { toaster.toast(it.message.toString()) }
                )
        }
    }

    private fun launchMovieViewer() {
        launchActivity(MovieViewerActivity::class)
        finish()
    }

    override fun getDisposable(): CompositeDisposable = CompositeDisposable()
}