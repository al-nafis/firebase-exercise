package com.example.firebase_exercise.regular_database.viewer

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebase_exercise.BaseActivity
import com.example.firebase_exercise.R
import com.example.firebase_exercise.databinding.ActivityRegularUserViewerBinding
import com.example.firebase_exercise.regular_database.add_user.RegularAddUserActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class RegularUserViewerActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: RegularUserViewerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRegularUserViewerBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_regular_user_viewer)
        binding.model = viewModel
        viewModel.addLifecycleObserver(this)
    }

    override fun getDisposable(): CompositeDisposable = CompositeDisposable().apply {
        add(viewModel.addUserScreenLaunchOrderChannel.subscribeBy(
            onNext = { if (it) launchActivity(RegularAddUserActivity::class) },
            onError = { it.printStackTrace() }
        ))
    }
}