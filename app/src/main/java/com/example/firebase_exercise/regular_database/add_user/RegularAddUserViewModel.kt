package com.example.firebase_exercise.regular_database.add_user

import androidx.databinding.ObservableField
import com.example.firebase_exercise.BaseViewModel
import com.example.firebase_exercise.data.User
import com.example.firebase_exercise.events.Toaster
import com.example.firebase_exercise.regular_database.FirebaseManager
import com.example.firebase_exercise.regular_database.UsersDataCallBack
import com.google.firebase.database.DatabaseError
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class RegularAddUserViewModel @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val toaster: Toaster
) : BaseViewModel() {

    val closeScreenOrderChannel: PublishSubject<Boolean> = PublishSubject.create<Boolean>()

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
                User(
                    name = nameField.get()!!,
                    age = ageField.get()!!,
                    email = emailField.get()!!
                ),
                object :
                    UsersDataCallBack {
                    override fun userExists() =
                        toaster.toast("User Exists")

                    override fun onSuccessAddingNewUser() {
                        toaster.toast("New User Added")
                        closeScreenOrderChannel.onNext(true)
                    }

                    override fun onCancelled(error: DatabaseError) =
                        toaster.toast(error.details)
                })
        }
    }
}