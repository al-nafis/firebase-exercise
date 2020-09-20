package com.example.firebase_exercise

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase_exercise.movie_viewer.MovieViewerAdapter

@BindingAdapter("setAdapter")
fun RecyclerView.setAdapter(movieViewerAdapter: MovieViewerAdapter) {
    adapter = movieViewerAdapter
}