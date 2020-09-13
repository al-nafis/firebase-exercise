package com.example.firebase_exercise.dagger_components

import com.example.firebase_exercise.MainApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Component(modules = [AndroidInjectionModule::class, SharedModule::class, ActivityModule::class])
interface MainApplicationComponent :AndroidInjector<MainApplication>