package com.example.firebase_exercise.common

import com.google.firebase.database.DatabaseError

interface MoviesDataCallBack {
    fun movieExists()
    fun onSuccessAddingNewMovie()
    fun onCancelled(error: DatabaseError)
}