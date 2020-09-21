package com.example.firebase_exercise.sign_in

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebase_exercise.BaseActivity
import com.example.firebase_exercise.R
import com.example.firebase_exercise.common.Toaster
import com.example.firebase_exercise.databinding.ActivitySignInBinding
import com.example.firebase_exercise.movie_viewer.MovieViewerActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class SignInActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: SignInViewModel

    @Inject
    lateinit var toaster: Toaster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySignInBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        binding.model = viewModel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onSignInRequest(requestCode, data)
    }

    override fun getDisposable(): CompositeDisposable = CompositeDisposable().apply {
        add(viewModel.signInAttemptInitializerChannel.subscribeBy {
            startActivityForResult(it.first.signInIntent, it.second)
        })
        add(viewModel.signInCompleteNotifierChannel.subscribeBy {
            launchActivity(MovieViewerActivity::class)
            finish()
        })
    }
}