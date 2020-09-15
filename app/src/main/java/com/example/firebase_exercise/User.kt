package com.example.firebase_exercise

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User(var name: String = "", var age: String = "", var email: String = "")