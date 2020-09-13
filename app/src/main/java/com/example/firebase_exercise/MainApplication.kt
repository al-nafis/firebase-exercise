package com.example.firebase_exercise

import android.app.Activity
import android.app.Application
import com.example.firebase_exercise.dagger_components.DaggerMainApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MainApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerMainApplicationComponent.builder().build().inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}