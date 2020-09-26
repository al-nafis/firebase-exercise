package com.example.firebase_exercise.movie_viewer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.example.firebase_exercise.BaseViewModel
import com.example.firebase_exercise.common.FirebaseAuthManager
import com.example.firebase_exercise.common.RealtimeDatabaseManager
import com.example.firebase_exercise.common.Toaster
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class RealtimeDatabaseMovieViewerViewModel @Inject constructor(
    private val realtimeDatabaseManager: RealtimeDatabaseManager,
    val adapter: MovieViewerAdapter
) : BaseViewModel() {

    val addMovieScreenLaunchOrderChannel: PublishSubject<Boolean> = PublishSubject.create<Boolean>()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        addDisposable(realtimeDatabaseManager.getMovieDataObservable().subscribeBy(
            onNext = { adapter.setMovies(it.sortedBy { movie -> movie.title }) }
        ))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        disposableClear()
    }

    fun addNewMovie() = addMovieScreenLaunchOrderChannel.onNext(true)
}
