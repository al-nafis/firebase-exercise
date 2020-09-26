package com.example.firebase_exercise

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.firebase_exercise.common.FirebaseAuthManager
import com.example.firebase_exercise.common.Toaster
import com.example.firebase_exercise.movie_viewer.FirestoreMovieViewerActivity
import com.example.firebase_exercise.movie_viewer.RealtimeDatabaseMovieViewerActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var firebaseAuthManager: FirebaseAuthManager

    @Inject
    lateinit var toaster: Toaster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun getDisposable() = CompositeDisposable()

    fun View.launchRealtimeDatabaseView() {
        launchActivity(RealtimeDatabaseMovieViewerActivity::class)
    }

    fun View.launchFirestoreDatabaseView() {
        launchActivity(FirestoreMovieViewerActivity::class)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.sign_out_menu) {
            firebaseAuthManager.userSignOut().subscribeBy(
                onComplete = {
                    launchActivity(SignInActivity::class)
                    overridePendingTransition(R.anim.on_press_back_enter, R.anim.on_press_back_exit)
                    finish()
                },
                onError = { toaster.toast(it.message.toString()) }
            )
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}