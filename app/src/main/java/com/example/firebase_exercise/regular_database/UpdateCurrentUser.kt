package com.example.firebase_exercise.regular_database

import com.example.firebase_exercise.User

interface UpdateCurrentUser {
    fun updateUserInfo(users: List<User>)
}