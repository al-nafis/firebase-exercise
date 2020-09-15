package com.example.firebase_exercise

import android.content.Context
import android.widget.Toast
import com.example.firebase_exercise.dagger_components.ApplicationContext
import javax.inject.Inject

class Toaster @Inject constructor(@ApplicationContext private val context: Context){
    fun toast(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}