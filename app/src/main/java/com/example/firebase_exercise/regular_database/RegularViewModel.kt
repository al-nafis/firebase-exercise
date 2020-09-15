package com.example.firebase_exercise.regular_database

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.example.firebase_exercise.BaseViewModel
import com.example.firebase_exercise.FirebaseManager
import com.example.firebase_exercise.User
import javax.inject.Inject

class RegularViewModel @Inject constructor(private val firebaseManager: FirebaseManager) :BaseViewModel(), UpdateCurrentUser {
    val nameField = ObservableField<String>("")
    val ageField = ObservableField<String>("")
    val emailField = ObservableField<String>("")
    private var userList: List<User> = listOf()

    fun addUser() {
        var isDuplicate = false
        for (user in userList) {
            if (user.email == emailField.get()) {
                isDuplicate = true
                break
            }
        }
        if (isDuplicate) {
            Log.d("ZARAH", "User Already Exists")
        } else {
            if (nameField.get().isNullOrEmpty() ||
                ageField.get().isNullOrEmpty() ||
                emailField.get().isNullOrEmpty()) {
                Log.d("ZARAH", "All fields required!")
            } else {
                firebaseManager.addUser(User().apply {
                    name = nameField.get()!!
                    age = ageField.get()!!
                    email = emailField.get()!!
                })
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        firebaseManager.listenToUsers(this)
    }

    override fun updateUserInfo(users: List<User>) {
        users.forEach {
            Log.d("ZARAH", "userName: ${it.name} userAge: ${it.age} userEmail: ${it.email}")
        }
        userList = users
    }
}