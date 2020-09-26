package com.example.firebase_exercise

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebase_exercise.common.FirebaseAuthManager
import com.example.firebase_exercise.common.Toaster
import com.example.firebase_exercise.databinding.ActivitySignInBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class SignInActivity : BaseActivity() {

    @Inject
    lateinit var firebaseAuthManager: FirebaseAuthManager

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
        startActivityForResult(firebaseAuthManager.googleSignInClient.signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && data != null) {
            firebaseAuthManager.signInToFirebaseWithGoogle(data)
                .subscribeBy(
                    onComplete = { launchMovieViewer() },
                    onError = { toaster.toast(it.message.toString()) }
                )
        }
    }

    private fun launchMovieViewer() {
        launchActivity(MainActivity::class)
        finish()
    }

    override fun getDisposable(): CompositeDisposable = CompositeDisposable()
}