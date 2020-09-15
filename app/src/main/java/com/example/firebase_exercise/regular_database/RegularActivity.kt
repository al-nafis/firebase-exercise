package com.example.firebase_exercise.regular_database

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.example.firebase_exercise.BaseActivity
import com.example.firebase_exercise.R
import com.example.firebase_exercise.databinding.ActivityRegularBinding
import dagger.android.AndroidInjection
import javax.inject.Inject

class RegularActivity : BaseActivity() {

    @Inject
    lateinit var regularViewModel: RegularViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRegularBinding = DataBindingUtil.setContentView(this, R.layout.activity_regular)
        binding.model = regularViewModel
        regularViewModel.addLifecycleObserver(this)
    }
}