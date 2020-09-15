package com.example.firebase_exercise

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebase_exercise.databinding.ActivityMainBinding
import com.example.firebase_exercise.regular_database.RegularActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this
    }

    fun launchRegularDatabase() {
        startActivity(Intent(this, RegularActivity::class.java))
    }
}