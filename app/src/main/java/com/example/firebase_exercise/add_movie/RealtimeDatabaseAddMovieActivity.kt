package com.example.firebase_exercise.add_movie

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebase_exercise.BaseActivity
import com.example.firebase_exercise.R
import com.example.firebase_exercise.databinding.ActivityRealtimeDatabaseAddMovieBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class RealtimeDatabaseAddMovieActivity : BaseActivity() {

    @Inject
    lateinit var viewModelRealtimeDatabase: RealtimeDatabaseAddMovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRealtimeDatabaseAddMovieBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_realtime_database_add_movie)
        binding.model = viewModelRealtimeDatabase
        viewModelRealtimeDatabase.addLifecycleObserver(this)
    }

    override fun getDisposable(): CompositeDisposable = CompositeDisposable().apply {
        add(viewModelRealtimeDatabase.closeScreenOrderChannel.subscribeBy(
            onNext = {
                finish()
                overridePendingTransition(R.anim.on_press_back_enter, R.anim.on_press_back_exit)
            },
            onError = { it.printStackTrace() }
        ))
    }
}