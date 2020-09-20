package com.example.firebase_exercise.common

import android.content.Context
import androidx.annotation.StringRes
import com.example.firebase_exercise.dagger_components.ApplicationContext
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext private val context: Context) {
    fun getString(@StringRes id: Int) = context.getString(id)
}