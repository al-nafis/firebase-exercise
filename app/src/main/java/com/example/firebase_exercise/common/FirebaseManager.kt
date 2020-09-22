package com.example.firebase_exercise.common

import android.content.Intent
import com.example.firebase_exercise.dagger_components.MovieDatabase
import com.example.firebase_exercise.models.Movie
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableEmitter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class FirebaseManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @MovieDatabase private val databaseReference: DatabaseReference,
    val googleSignInClient: GoogleSignInClient
) {

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

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
                firebaseAuth.currentUser?.let { currentUser ->
                    currentUser.email?.let { userEmail ->
                        databaseReference.child(userEmail.getUserChild())
                            .addValueEventListener(this)
                        emitter.setCancellable {
                            databaseReference.child(userEmail.getUserChild())
                                .removeEventListener(this)
                        }
                    }
                }
            }
        }.map { getMovieList(it) }.addSchedulers()

    fun addMovie(newMovie: Movie): Completable = Completable.create { emitter ->
        firebaseAuth.currentUser?.let { currentUser ->
            currentUser.email?.let { userEmail ->
                databaseReference.child(userEmail.getUserChild())
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            emitter.onError(error.toException())
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (movieExistsInDatabase(snapshot, newMovie)) {
                                emitter.onError(MovieExistenceException())
                            } else {
                                insertNewMovie(userEmail, newMovie, emitter)
                            }
                        }
                    })
            }
        }
    }.addSchedulers()

    private fun insertNewMovie(
        userEmail: String,
        newMovie: Movie,
        emitter: @NonNull CompletableEmitter
    ) {
        databaseReference.child(userEmail.getUserChild())
            .push().setValue(newMovie)
            .addOnSuccessListener {
                emitter.onComplete()
            }
            .addOnFailureListener {
                it.printStackTrace()
                emitter.onError(MovieAddingFailedException())
            }
    }

    private fun movieExistsInDatabase(snapshot: DataSnapshot, newMovie: Movie): Boolean {
        for (movie in getMovieList(snapshot)) {
            if (movie.title.toLowerCase() == newMovie.title.toLowerCase()
                && movie.year.toLowerCase() == newMovie.year.toLowerCase()
            ) return true
        }
        return false
    }

    fun signInToFirebaseWithGoogle(data: Intent): Completable =
        Completable.create { emitter ->
            try {
                val account: GoogleSignInAccount? = GoogleSignIn.getSignedInAccountFromIntent(data)
                    .getResult(ApiException::class.java)
                account?.let {
                    val credential = GoogleAuthProvider.getCredential(it.idToken, null)
                    firebaseAuth.signInWithCredential(credential).addOnCompleteListener { signIn ->
                        if (signIn.isSuccessful) {
                            emitter.onComplete()
                        } else {
                            emitter.onError(UserSignInFailedException())
                        }
                    }
                }
            } catch (e: Exception) {
                emitter.onError(UserSignInFailedException())
            }
        }.addSchedulers()

    fun userSignOut(): Completable = Completable.create { emitter ->
        firebaseAuth.signOut()
        googleSignInClient.signOut().addOnCompleteListener { signOut ->
            if (signOut.isSuccessful) {
                emitter.onComplete()
            } else {
                emitter.onError(UserSignOutFailedException())
            }
        }
    }.addSchedulers()

    private fun getMovieList(snapshot: DataSnapshot): List<Movie> {
        return if (snapshot.exists()) {
            val t: GenericTypeIndicator<Map<String, Movie>> =
                object : GenericTypeIndicator<Map<String, Movie>>() {}
            val map = snapshot.getValue(t) as Map<String, Movie>
            map.values.toList()
        } else {
            listOf()
        }
    }

    private fun String.getUserChild() = this.replace(Regex("[.#$\\[\\]]"), "")

    private fun Completable.addSchedulers(): Completable =
        this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    private fun <T> Observable<T>.addSchedulers(): Observable<T> =
        this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

class MovieAddingFailedException : Exception("Movie Adding Failed!")
class MovieExistenceException : Exception("Movie Exists")
class UserSignInFailedException : Exception("Sign In Failed")
class UserSignOutFailedException : Exception("Sign Out Failed")
