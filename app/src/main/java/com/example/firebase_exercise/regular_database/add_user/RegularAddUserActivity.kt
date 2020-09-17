package com.example.firebase_exercise.regular_database.add_user

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebase_exercise.BaseActivity
import com.example.firebase_exercise.R
import com.example.firebase_exercise.databinding.ActivityRegularAddUserBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class RegularAddUserActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: RegularAddUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRegularAddUserBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_regular_add_user)
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