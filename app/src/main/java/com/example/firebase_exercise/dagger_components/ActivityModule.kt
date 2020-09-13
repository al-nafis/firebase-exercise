package com.example.firebase_exercise.dagger_components

import com.example.firebase_exercise.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
@ContributesAndroidInjector
abstract fun providesMainActivity(): MainActivity
}