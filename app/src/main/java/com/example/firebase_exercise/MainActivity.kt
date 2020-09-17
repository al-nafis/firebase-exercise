package com.example.firebase_exercise

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebase_exercise.databinding.ActivityMainBinding
import com.example.firebase_exercise.regular_database.add_user.RegularAddUserActivity
import com.example.firebase_exercise.regular_database.viewer.RegularUserViewerActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this
    }

    override fun getDisposable(): CompositeDisposable =
        CompositeDisposable()

    fun launchRegularDatabase() {
        startActivity(Intent(this, RegularUserViewerActivity::class.java))
    }
}