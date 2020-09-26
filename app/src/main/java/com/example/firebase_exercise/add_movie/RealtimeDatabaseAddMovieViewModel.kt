package com.example.firebase_exercise.add_movie

import androidx.databinding.ObservableField
import com.example.firebase_exercise.BaseViewModel
import com.example.firebase_exercise.common.RealtimeDatabaseManager
import com.example.firebase_exercise.common.Toaster
import com.example.firebase_exercise.models.Movie
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class RealtimeDatabaseAddMovieViewModel @Inject constructor(
    private val realtimeDatabaseManager: RealtimeDatabaseManager,
    private val toaster: Toaster
) : BaseViewModel() {

    val closeScreenOrderChannel: PublishSubject<Boolean> = PublishSubject.create<Boolean>()

    val titleField = ObservableField<String>("")
    val yearField = ObservableField<String>("")

    fun addMovie() {
        titleField.get()?.let { title ->
            yearField.get()?.let { year ->
                if (title.isEmpty() || year.isEmpty()) {
                    toaster.toast("All fields required!")
                } else {
                    addDisposable(realtimeDatabaseManager.addNewMovie(Movie(title, year))
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
    }
}