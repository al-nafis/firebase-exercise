package com.example.firebase_exercise.sign_in

import com.example.firebase_exercise.BaseViewModel
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class SignInViewModel @Inject constructor() : BaseViewModel() {

    val signInOrderChannel: PublishSubject<Boolean> = PublishSubject.create()

    fun signInWithGoogle() {
        signInOrderChannel.onNext(true)
    }
}