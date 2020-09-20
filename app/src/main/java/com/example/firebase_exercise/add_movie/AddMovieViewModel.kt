package com.example.firebase_exercise.add_movie

import androidx.databinding.ObservableField
import com.example.firebase_exercise.BaseViewModel
import com.example.firebase_exercise.data.Movie
import com.example.firebase_exercise.events.Toaster
import com.example.firebase_exercise.FirebaseManager
import com.example.firebase_exercise.MoviesDataCallBack
import com.google.firebase.database.DatabaseError
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
        if (titleField.get().isNullOrEmpty() ||
            yearField.get().isNullOrEmpty()
        ) {
            toaster.toast("All fields required!")
        } else {
            firebaseManager.addMovie(
                Movie(
                    title = titleField.get()!!,
                    year = yearField.get()!!
                ),
                object :
                    MoviesDataCallBack {
                    override fun movieExists() =
                        toaster.toast("Movie Exists")

                    override fun onSuccessAddingNewMovie() {
                        toaster.toast("New Movie Added")
                        closeScreenOrderChannel.onNext(true)
                    }

                    override fun onCancelled(error: DatabaseError) =
                        toaster.toast(error.details)
                })
        }
    }
}