package com.example.firebase_exercise

import android.content.SharedPreferences
import androidx.annotation.StringDef
import javax.inject.Inject

class SharedPrefUtil @Inject constructor(private val sharedPreferences: SharedPreferences) {

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(DEFAULT_VALUE)
    annotation class SharedPrefKey

    companion object {
        const val DEFAULT_VALUE = "defaultValue"
        const val ANONYMOUS = "anonymous"
        const val USER = "user"
    }

    fun setUser(user: String) = setString(USER, user)
    fun getUser() : String = getString(USER, ANONYMOUS)

    private fun setString(@SharedPrefKey key: String, value: String) =
        sharedPreferences.edit().putString(key, value).apply()
    private fun getString(@SharedPrefKey key: String, defaultValue: String = DEFAULT_VALUE): String =
        sharedPreferences.getString(key, defaultValue)!!
}