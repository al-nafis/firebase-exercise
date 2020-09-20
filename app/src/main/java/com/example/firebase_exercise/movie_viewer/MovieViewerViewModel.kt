package com.example.firebase_exercise.movie_viewer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.example.firebase_exercise.BaseViewModel
import com.example.firebase_exercise.data.Movie
import com.example.firebase_exercise.events.Toaster
import com.example.firebase_exercise.FirebaseManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class MovieViewerViewModel @Inject constructor(
    private val manager: FirebaseManager,
    private val toaster: Toaster,
    val adapter: MovieViewerAdapter
) : BaseViewModel() {

    val addMovieScreenLaunchOrderChannel: PublishSubject<Boolean> = PublishSubject.create<Boolean>()

    private val listener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            toaster.toast(error.details)
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val t: GenericTypeIndicator<Map<String, Movie>> =
                    object : GenericTypeIndicator<Map<String, Movie>>() {}
                val map = snapshot.getValue(t) as Map<String, Movie>
                val movies = mutableListOf<Movie>()
                for (movie in map.values) {
                    movies.add(movie)
                }
                adapter.setMovies(movies.sortedBy { it.title })
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        manager.listenToMovieDataChange(listener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        manager.removeListener(listener)
    }

    fun addNewMovie() = addMovieScreenLaunchOrderChannel.onNext(true)
}
