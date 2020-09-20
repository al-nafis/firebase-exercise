package com.example.firebase_exercise.dagger_components

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
class SharedModule {
    @Provides
    @MovieDatabase
    fun provideMoviesFirebaseDatabase(): DatabaseReference =
        FirebaseDatabase.getInstance().getReference("movies")
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MovieDatabase