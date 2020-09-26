package com.example.firebase_exercise.movie_viewer

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.example.firebase_exercise.BaseViewModel
import com.example.firebase_exercise.common.FirebaseAuthManager
import com.example.firebase_exercise.common.FirestoreDatabaseManager
import com.example.firebase_exercise.common.Toaster
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class FirestoreMovieViewerViewModel @Inject constructor(
    private val firestoreDatabaseManager: FirestoreDatabaseManager,
    val adapter: MovieViewerAdapter
) : BaseViewModel() {

    val addMovieScreenLaunchOrderChannel: PublishSubject<Boolean> = PublishSubject.create<Boolean>()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        addDisposable(firestoreDatabaseManager.getMoviesObservable().subscribeBy(
            onNext = { adapter.setMovies(it) },
            onError = { Log.d("ZARAH", it.message.toString()) }
        ))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        disposableClear()
    }

    fun addNewMovie() {
        addMovieScreenLaunchOrderChannel.onNext(true)
    }
}