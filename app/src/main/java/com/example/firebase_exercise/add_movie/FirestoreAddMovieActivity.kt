package com.example.firebase_exercise.add_movie

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebase_exercise.BaseActivity
import com.example.firebase_exercise.R
import com.example.firebase_exercise.databinding.ActivityFirestoreAddMovieBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class FirestoreAddMovieActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: FirestoreAddMovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFirestoreAddMovieBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_firestore_add_movie)
        binding.model = viewModel
        viewModel.addLifecycleObserver(this)
    }

    override fun getDisposable() = CompositeDisposable().apply {
        add(viewModel.closeScreenOrderChannel.subscribeBy(
            onNext = {
                finish()
                overridePendingTransition(R.anim.on_press_back_enter, R.anim.on_press_back_exit)
            },
            onError = { it.printStackTrace() }
        ))
    }
}