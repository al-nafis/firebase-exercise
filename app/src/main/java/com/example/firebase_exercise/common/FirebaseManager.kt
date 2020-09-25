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
                firebaseAuth.currentUser?.let { user ->
                    user.email?.let { userEmail ->
                        databaseReference.child(userEmail.convertToFirebaseChildString())
                            .addValueEventListener(this)
                        emitter.setCancellable {
                            databaseReference.child(userEmail.convertToFirebaseChildString())
                                .removeEventListener(this)
                        }
                    }
                }
            }
        }.map { it.getMovieList() }.addSchedulers()


    fun addNewMovie(newMovie: Movie): Completable = Completable.create { emitter ->
        firebaseAuth.currentUser?.let { currentUser ->
            currentUser.email?.let { userEmail ->
                databaseReference.child(userEmail.convertToFirebaseChildString())
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

    fun signInToFirebaseWithGoogle(data: Intent): Completable =
        Completable.create { emitter ->
            try {
                val account: GoogleSignInAccount = GoogleSignIn.getSignedInAccountFromIntent(data)
                    .getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                firebaseAuth.signInWithCredential(credential).addOnCompleteListener { signInTask ->
                    if (signInTask.isSuccessful) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(UserSignInFailedException())
                    }
                }
            } catch (e: ApiException) {
                emitter.onError(UserSignInFailedException())
            }
        }.addSchedulers()

    fun userSignOut(): Completable =
        Completable.create { emitter ->
            firebaseAuth.signOut()
            googleSignInClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(UserSignOutFailedException())
                }
            }
        }.addSchedulers()

    private fun insertMovie(userEmail: String, movie: Movie, emitter: CompletableEmitter) {
        databaseReference.child(userEmail.convertToFirebaseChildString())
            .push().setValue(movie)
            .addOnSuccessListener {
                emitter.onComplete()
            }
            .addOnFailureListener {
                emitter.onError(MovieAddingFailedException())
            }
    }

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
    private fun Completable.addSchedulers(): Completable =
        this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    private fun <T> Observable<T>.addSchedulers(): Observable<T> =
        this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

class MovieAddingFailedException : Exception("Movie Adding Failed!")
class MovieExistenceException : Exception("Movie Exists")
class UserSignInFailedException : Exception("Sign In Failed")
class UserSignOutFailedException : Exception("Sign Out Failed")
