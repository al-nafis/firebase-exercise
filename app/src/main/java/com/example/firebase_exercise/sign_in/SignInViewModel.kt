package com.example.firebase_exercise.sign_in

import android.content.Intent
import com.example.firebase_exercise.BaseViewModel
import com.example.firebase_exercise.common.FirebaseManager
import com.example.firebase_exercise.common.Toaster
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val toaster: Toaster
) :
    BaseViewModel() {

    private val RC_SIGN_IN = 9001

    val signInAttemptInitializerChannel: PublishSubject<Pair<GoogleSignInClient, Int>> =
        PublishSubject.create()
    val signInCompleteNotifierChannel: PublishSubject<Boolean> = PublishSubject.create()

    fun signInWithGoogle() {
        signInAttemptInitializerChannel.onNext(Pair(firebaseManager.googleSignInClient, RC_SIGN_IN))
    }

    fun onSignInRequest(requestCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && data != null) {
            firebaseManager.signInToFirebaseWithGoogle(data)
                .subscribeBy(
                    onComplete = { signInCompleteNotifierChannel.onNext(true) },
                    onError = { toaster.toast("Sign In Failed") }
                )
        }
    }
}