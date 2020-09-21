package com.example.firebase_exercise.add_movie

import androidx.databinding.ObservableField
import com.example.firebase_exercise.BaseViewModel
import com.example.firebase_exercise.common.FirebaseManager
import com.example.firebase_exercise.common.Toaster
import com.example.firebase_exercise.models.Movie
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class AddMovieViewModel @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val toaster: Toaster
) : BaseViewModel() {

    val closeScreenOrderChannel: PublishSubject<Boolean> = PublishSubject.create<Boolean>()

    val titleField = ObservableField<String>("")
    val yearField = ObservableField<String>("")

    fun addMovie() {
        if (titleField.get().isNullOrEmpty() || yearField.get().isNullOrEmpty()) {
            toaster.toast("All fields required!")
        } else {
            addDisposable(firebaseManager.addMovie(Movie(titleField.get()!!, yearField.get()!!))
                .subscribeBy(
                    onComplete = {
                        toaster.toast("New Movie Added")
                        closeScreenOrderChannel.onNext(true)
                    },
                    onError = { toaster.toast(it.message.toString()) }
                ))
        }
    }
}