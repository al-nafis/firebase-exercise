package com.example.firebase_exercise.dagger_components

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
class ContextModule (private val context: Context) {
    @Provides
    @ApplicationContext
    internal fun getContext(): Context {
        return context
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationContext