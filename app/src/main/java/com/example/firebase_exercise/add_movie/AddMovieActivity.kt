package com.example.firebase_exercise.add_movie

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebase_exercise.BaseActivity
import com.example.firebase_exercise.R
import com.example.firebase_exercise.databinding.ActivityAddMovieBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class AddMovieActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: AddMovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAddMovieBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_add_movie)
        binding.model = viewModel
        viewModel.addLifecycleObserver(this)
    }

    override fun getDisposable(): CompositeDisposable = CompositeDisposable().apply {
        add(viewModel.closeScreenOrderChannel.subscribeBy(
            onNext = { finish() },
            onError = { it.printStackTrace() }
        ))
    }
}