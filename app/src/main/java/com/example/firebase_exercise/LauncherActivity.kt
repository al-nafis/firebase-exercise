package com.example.firebase_exercise

import android.os.Bundle
import com.example.firebase_exercise.common.FirebaseAuthManager
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class LauncherActivity : BaseActivity() {

    @Inject
    lateinit var firebaseAuthManager: FirebaseAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuthManager.getCurrentUser()?.let {
            launchActivity(MainActivity::class)
        } ?: run {
            launchActivity(SignInActivity::class)
        }
        finish()
    }

    override fun getDisposable(): CompositeDisposable =
        CompositeDisposable()
}