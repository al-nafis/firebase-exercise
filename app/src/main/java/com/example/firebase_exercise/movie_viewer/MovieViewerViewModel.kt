package com.example.firebase_exercise.movie_viewer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.example.firebase_exercise.BaseViewModel
import com.example.firebase_exercise.common.FirebaseManager
import com.example.firebase_exercise.common.Toaster
import com.example.firebase_exercise.models.Movie
import com.google.firebase.database.GenericTypeIndicator
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class MovieViewerViewModel @Inject constructor(
    private val manager: FirebaseManager,
    private val toaster: Toaster,
    val adapter: MovieViewerAdapter
) : BaseViewModel() {

    val addMovieScreenLaunchOrderChannel: PublishSubject<Boolean> = PublishSubject.create<Boolean>()
    val signOutOrderChannel: PublishSubject<Boolean> = PublishSubject.create()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        addDisposable(manager.getMovieDataObservable().subscribeBy(
            onNext = {
                if (it.exists()) {
                    val t: GenericTypeIndicator<Map<String, Movie>> =
                        object : GenericTypeIndicator<Map<String, Movie>>() {}
                    val movies = it.getValue(t) as Map<String, Movie>
                    adapter.setMovies(movies.values.sortedBy { movie -> movie.title })
                }
            },
            onError = { it.printStackTrace() }
        ))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        disposableClear()
    }

    fun addNewMovie() = addMovieScreenLaunchOrderChannel.onNext(true)

    fun onClickSignOut() {
        manager.userSignOut().subscribeBy(
            onComplete = { signOutOrderChannel.onNext(true) },
            onError = { toaster.toast(it.message.toString()) }
        )
    }
}
