package com.example.firebase_exercise.movie_viewer

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebase_exercise.BaseActivity
import com.example.firebase_exercise.R
import com.example.firebase_exercise.databinding.ActivityMovieViewerBinding
import com.example.firebase_exercise.add_movie.AddMovieActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class MovieViewerActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: MovieViewerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMovieViewerBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_movie_viewer)
        binding.model = viewModel
        viewModel.addLifecycleObserver(this)
    }

    override fun getDisposable(): CompositeDisposable = CompositeDisposable().apply {
        add(viewModel.addMovieScreenLaunchOrderChannel.subscribeBy(
            onNext = { if (it) launchActivity(AddMovieActivity::class) },
            onError = { it.printStackTrace() }
        ))
    }
}