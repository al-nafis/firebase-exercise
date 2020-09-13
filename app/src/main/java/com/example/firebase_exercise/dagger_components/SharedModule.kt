package com.example.firebase_exercise.dagger_components

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
class SharedModule {
    @Provides
    @UserDatabase
    fun provideUsersFirebaseDatabase(): DatabaseReference =
        FirebaseDatabase.getInstance().getReference("users")
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserDatabase