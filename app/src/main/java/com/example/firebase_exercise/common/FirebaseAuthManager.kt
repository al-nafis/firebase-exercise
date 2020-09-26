package com.example.firebase_exercise.common

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class FirebaseAuthManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    val googleSignInClient: GoogleSignInClient
) {

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

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
}