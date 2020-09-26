package com.example.firebase_exercise.movie_viewer

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebase_exercise.BaseActivity
import com.example.firebase_exercise.R
import com.example.firebase_exercise.add_movie.FirestoreAddMovieActivity
import com.example.firebase_exercise.databinding.ActivityFirestoreMovieViewerBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class FirestoreMovieViewerActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: FirestoreMovieViewerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFirestoreMovieViewerBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_firestore_movie_viewer)
        binding.model = viewModel
        viewModel.addLifecycleObserver(this)
    }

    override fun getDisposable() = CompositeDisposable().apply {
        add(viewModel.addMovieScreenLaunchOrderChannel.subscribeBy {
            launchActivity(FirestoreAddMovieActivity::class)
        })
    }
}