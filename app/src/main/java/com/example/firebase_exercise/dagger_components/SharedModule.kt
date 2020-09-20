package com.example.firebase_exercise.dagger_components

import android.content.Context
import android.content.SharedPreferences
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

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context) : SharedPreferences =
        context.getSharedPreferences("appPref", Context.MODE_PRIVATE)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MovieDatabase