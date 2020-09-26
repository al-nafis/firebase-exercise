package com.example.firebase_exercise.dagger_components

import com.example.firebase_exercise.LauncherActivity
import com.example.firebase_exercise.MainActivity
import com.example.firebase_exercise.SignInActivity
import com.example.firebase_exercise.add_movie.FirestoreAddMovieActivity
import com.example.firebase_exercise.add_movie.RealtimeDatabaseAddMovieActivity
import com.example.firebase_exercise.movie_viewer.FirestoreMovieViewerActivity
import com.example.firebase_exercise.movie_viewer.RealtimeDatabaseMovieViewerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun providesLauncherActivity(): LauncherActivity

    @ContributesAndroidInjector
    abstract fun providesMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun providesSignInActivity(): SignInActivity

    @ContributesAndroidInjector
    abstract fun providesRealtimeDatabaseMovieViewerActivity(): RealtimeDatabaseMovieViewerActivity

    @ContributesAndroidInjector
    abstract fun providesRealtimeDatabaseAddMovieActivity(): RealtimeDatabaseAddMovieActivity

    @ContributesAndroidInjector
    abstract fun providesFirestoreDataViewerActivity(): FirestoreMovieViewerActivity

    @ContributesAndroidInjector
    abstract fun providesFirestoreAddMovieActivity(): FirestoreAddMovieActivity
}