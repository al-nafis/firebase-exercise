package com.example.firebase_exercise

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import dagger.android.AndroidInjection
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlin.reflect.KClass

abstract class BaseActivity : AppCompatActivity(), LifecycleOwner {

    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        disposable = getDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    fun launchActivity(destination: KClass<out BaseActivity>) {
        startActivity(Intent(this, destination.java))
        overridePendingTransition(R.anim.enter, R.anim.exit)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.on_press_back_enter, R.anim.on_press_back_exit)
    }

    abstract fun getDisposable(): CompositeDisposable
}