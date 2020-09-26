package com.example.firebase_exercise.common

import com.example.firebase_exercise.models.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableEmitter
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RealtimeDatabaseManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val moviesRealtimeDatabase: DatabaseReference
) {

    fun getMovieDataObservable(): Observable<List<Movie>> =
        Observable.create<DataSnapshot> { emitter ->
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    emitter.onComplete()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    emitter.onNext(snapshot)
                }
            }.apply {
                firebaseAuth.currentUser?.let { user ->
                    user.email?.let { userEmail ->
                        getQueryPath(userEmail)
                            .addValueEventListener(this)
                        emitter.setCancellable {
                            getQueryPath(userEmail)
                                .removeEventListener(this)
                        }
                    }
                }
            }
        }.map { it.getMovieList() }.addSchedulers()


    fun addNewMovie(newMovie: Movie): Completable = Completable.create { emitter ->
        firebaseAuth.currentUser?.let { currentUser ->
            currentUser.email?.let { userEmail ->
                getQueryPath(userEmail)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            emitter.onError(error.toException())
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.movieExists(newMovie)) {
                                emitter.onError(MovieExistenceException())
                            } else {
                                insertMovie(userEmail, newMovie, emitter)
                            }
                        }
                    })
            }
        }

    }.addSchedulers()

    private fun insertMovie(userEmail: String, movie: Movie, emitter: CompletableEmitter) {
        getQueryPath(userEmail)
            .push().setValue(movie)
            .addOnSuccessListener {
                emitter.onComplete()
            }
            .addOnFailureListener {
                emitter.onError(MovieAddingFailedException())
            }
    }

    private fun getQueryPath(userEmail: String) =
        moviesRealtimeDatabase.child(userEmail.convertToFirebaseChildString())
            .child("movies")

    private fun DataSnapshot.movieExists(newMovie: Movie): Boolean {
        for (movie in getMovieList()) {
            if (movie.title == newMovie.title && movie.year == newMovie.year) {
                return true
            }
        }
        return false
    }

    private fun DataSnapshot.getMovieList(): List<Movie> {
        return if (exists()) {
            val t: GenericTypeIndicator<Map<String, Movie>> =
                object : GenericTypeIndicator<Map<String, Movie>>() {}
            val map = getValue(t) as Map<String, Movie>
            map.values.toList()
        } else {
            listOf()
        }
    }

    private fun String.convertToFirebaseChildString() = this.replace(Regex("[.#$\\[\\]]"), "")
}
