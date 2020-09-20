package com.example.firebase_exercise.sign_in

import android.content.Intent
import com.example.firebase_exercise.BaseViewModel
import com.example.firebase_exercise.common.SharedPrefUtil
import com.example.firebase_exercise.common.Toaster
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,
    private val sharedPrefUtil: SharedPrefUtil,
    private val toaster: Toaster
) :
    BaseViewModel() {

    private val RC_SIGN_IN = 9001

    val signInOrderChannel: PublishSubject<Pair<GoogleSignInClient, Int>> = PublishSubject.create()
    val signInCompleteNotifierChannel: PublishSubject<Boolean> = PublishSubject.create()

    fun signInWithGoogle() {
        signInOrderChannel.onNext(Pair(googleSignInClient, RC_SIGN_IN))
    }

    fun onSignInRequest(requestCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
                signInToFirebaseWithGoogle(account)
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }

    private fun signInToFirebaseWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    sharedPrefUtil.setUser(firebaseAuth.currentUser!!.email!!)
                    signInCompleteNotifierChannel.onNext(true)
                } else {
                    toaster.toast("Sign In Failed")
                }
            }
    }
}