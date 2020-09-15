package com.example.firebase_exercise

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

abstract class BaseViewModel : LifecycleObserver {
    fun addLifecycleObserver(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }
}