package com.example.firebase_exercise.dagger_components

import com.example.firebase_exercise.MainActivity
import com.example.firebase_exercise.regular_database.add_user.RegularAddUserActivity
import com.example.firebase_exercise.regular_database.viewer.RegularUserViewerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun providesMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun providesRegularAddUserActivity(): RegularAddUserActivity

    @ContributesAndroidInjector
    abstract fun providesRegularUserViewerActivity(): RegularUserViewerActivity
}