package com.example.firebasetutorial.dagger_components

import com.example.firebasetutorial.MainApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Component(modules = [AndroidInjectionModule::class, SharedModule::class, ActivityModule::class])
interface MainApplicationComponent :AndroidInjector<MainApplication>