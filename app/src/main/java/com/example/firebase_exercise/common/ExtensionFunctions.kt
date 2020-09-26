package com.example.firebase_exercise.common

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

fun Completable.addSchedulers(): Completable =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.addSchedulers(): Observable<T> =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())