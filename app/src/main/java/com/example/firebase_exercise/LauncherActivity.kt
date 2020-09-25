package com.example.firebase_exercise

import android.os.Bundle
import com.example.firebase_exercise.common.FirebaseManager
import com.example.firebase_exercise.movie_viewer.MovieViewerActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class LauncherActivity : BaseActivity() {

    @Inject
    lateinit var firebaseManager: FirebaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseManager.getCurrentUser()?.let {
            launchActivity(MovieViewerActivity::class)
        } ?: run {
            launchActivity(SignInActivity::class)
        }
        finish()
    }

    override fun getDisposable(): CompositeDisposable =
        CompositeDisposable()
}