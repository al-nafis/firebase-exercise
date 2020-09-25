package com.example.firebase_exercise.dagger_components

import com.example.firebase_exercise.LauncherActivity
import com.example.firebase_exercise.add_movie.AddMovieActivity
import com.example.firebase_exercise.movie_viewer.MovieViewerActivity
import com.example.firebase_exercise.SignInActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun providesMainActivity(): LauncherActivity

    @ContributesAndroidInjector
    abstract fun providesAddMovieActivity(): AddMovieActivity

    @ContributesAndroidInjector
    abstract fun providesMovieViewerActivity(): MovieViewerActivity

    @ContributesAndroidInjector
    abstract fun providesSignInActivity(): SignInActivity
}