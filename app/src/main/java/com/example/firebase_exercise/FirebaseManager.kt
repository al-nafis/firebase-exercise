package com.example.firebase_exercise

import com.example.firebase_exercise.dagger_components.MovieDatabase
import com.example.firebase_exercise.data.Movie
import com.google.firebase.database.*
import javax.inject.Inject

class FirebaseManager @Inject constructor(
    @MovieDatabase private val databaseReference: DatabaseReference,
    private val sharedPrefUtil: SharedPrefUtil
) {

    private fun String.getUserChild() = this.replace(Regex("[.#$\\[\\]]"), "")

    fun listenToMovieDataChange(listener: ValueEventListener) {
        databaseReference.child(sharedPrefUtil.getUser().getUserChild())
            .addValueEventListener(listener)
    }

    fun removeListener(listener: ValueEventListener) =
        databaseReference.child(sharedPrefUtil.getUser().getUserChild())
            .removeEventListener(listener)

    fun addMovie(newMovie: Movie, moviesDataCallBack: MoviesDataCallBack) {
        databaseReference.child(sharedPrefUtil.getUser().getUserChild())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    moviesDataCallBack.onCancelled(error)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val t: GenericTypeIndicator<Map<String, Movie>> =
                            object : GenericTypeIndicator<Map<String, Movie>>() {}
                        val map = snapshot.getValue(t) as Map<String, Movie>
                        for (movie in map.values) {
                            if (movie.title == newMovie.title && movie.year == newMovie.year) {
                                moviesDataCallBack.movieExists()
                                return
                            }
                        }
                    }
                    databaseReference.child(sharedPrefUtil.getUser().getUserChild())
                        .push().setValue(newMovie) { _, _ ->
                            moviesDataCallBack.onSuccessAddingNewMovie()
                        }
                }
            })
    }
}