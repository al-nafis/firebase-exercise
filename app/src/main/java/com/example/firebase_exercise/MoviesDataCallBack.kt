package com.example.firebase_exercise

import com.google.firebase.database.DatabaseError

interface MoviesDataCallBack {
    fun movieExists()
    fun onSuccessAddingNewMovie()
    fun onCancelled(error: DatabaseError)
}