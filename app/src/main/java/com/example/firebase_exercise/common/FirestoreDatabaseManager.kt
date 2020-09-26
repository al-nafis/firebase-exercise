package com.example.firebase_exercise.common

import com.example.firebase_exercise.models.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableEmitter
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class FirestoreDatabaseManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestoreUsers: CollectionReference
) {

    fun getMoviesObservable(): Observable<List<Movie>> =
        Observable.create<QuerySnapshot> { emitter ->
            firebaseAuth.currentUser?.let { currentUser ->
                currentUser.email?.let { userEmail ->
                    val listenerRegistration = getQueryPath(userEmail)
                        .addSnapshotListener { snapshot, error ->
                            error?.let {
                                emitter.onError(error)
                                return@addSnapshotListener
                            } ?: run {
                                emitter.onNext(snapshot)
                            }
                        }
                    emitter.setCancellable {
                        listenerRegistration.remove()
                    }
                }
            }
        }.map { it.getMovieList() }.addSchedulers()

    fun addNewMovie(newMovie: Movie): Completable = Completable.create { emitter ->
        firebaseAuth.currentUser?.let { currentUser ->
            currentUser.email?.let { userEmail ->
                getQueryPath(userEmail).get()
                    .addOnSuccessListener {
                        if (it.movieExists(newMovie)) {
                            emitter.onError(MovieExistenceException())
                        } else {
                            insertMovie(userEmail, newMovie, emitter)
                        }
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }
            }
        }
    }.addSchedulers()

    private fun insertMovie(userEmail: String, newMovie: Movie, emitter: CompletableEmitter) {
        val movie = hashMapOf(
            "title" to newMovie.title,
            "year" to newMovie.year
        )
        getQueryPath(userEmail).add(movie)
            .addOnCompleteListener { emitter.onComplete() }
            .addOnFailureListener { emitter.onError(MovieAddingFailedException()) }
    }

    private fun QuerySnapshot.movieExists(newMovie: Movie): Boolean {
        for (movie in getMovieList()) {
            if (movie.title == newMovie.title && movie.year == newMovie.year) {
                return true
            }
        }
        return false
    }

    private fun QuerySnapshot.getMovieList(): List<Movie> =
        if (this != null && !this.isEmpty)
            this.toObjects(Movie::class.java)
        else
            listOf()


    private fun getQueryPath(userEmail: String) =
        firestoreUsers.document(userEmail).collection("movies")
}