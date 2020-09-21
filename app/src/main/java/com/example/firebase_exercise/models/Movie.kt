package com.example.firebase_exercise.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Movie(var title: String = "", var year: String = "")