package com.example.firebase_exercise.regular_database

import androidx.databinding.ObservableField
import com.example.firebase_exercise.BaseViewModel
import com.example.firebase_exercise.FirebaseManager
import com.example.firebase_exercise.Toaster
import com.example.firebase_exercise.User
import com.google.firebase.database.DatabaseError
import javax.inject.Inject

class RegularViewModel @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val toaster: Toaster
) : BaseViewModel() {
    val nameField = ObservableField<String>("")
    val ageField = ObservableField<String>("")
    val emailField = ObservableField<String>("")

    fun addUser() {
        if (nameField.get().isNullOrEmpty() ||
            ageField.get().isNullOrEmpty() ||
            emailField.get().isNullOrEmpty()
        ) {
            toaster.toast("All fields required!")
        } else {
            firebaseManager.addUser(
                User(name = nameField.get()!!, age = ageField.get()!!, email = emailField.get()!!),
                object : UsersDataCallBack {
                    override fun userExists() {
                        toaster.toast("User Exists")
                    }

                    override fun onSuccessAddingNewUser() {
                        toaster.toast("New User Added")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        toaster.toast(error.details)
                    }
                })
        }
    }
}