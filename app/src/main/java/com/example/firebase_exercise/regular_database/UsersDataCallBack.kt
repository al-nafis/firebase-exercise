package com.example.firebase_exercise.regular_database

import com.google.firebase.database.DatabaseError

interface UsersDataCallBack {
    fun userExists()
    fun onSuccessAddingNewUser()
    fun onCancelled(error: DatabaseError)
}