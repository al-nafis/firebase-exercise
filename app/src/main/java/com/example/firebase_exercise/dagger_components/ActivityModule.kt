package com.example.firebase_exercise.dagger_components

import com.example.firebase_exercise.MainActivity
import com.example.firebase_exercise.regular_database.RegularActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun providesMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun providesRegularActivity(): RegularActivity
}