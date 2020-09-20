package com.example.firebase_exercise

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebase_exercise.databinding.ActivityMainBinding
import com.example.firebase_exercise.movie_viewer.MovieViewerActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this
    }

    override fun getDisposable(): CompositeDisposable =
        CompositeDisposable()

    fun launchMovieDatabase() {
        startActivity(Intent(this, MovieViewerActivity::class.java))
    }
}