package com.example.firebase_exercise

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase_exercise.regular_database.viewer.UserViewerAdapter

@BindingAdapter("setAdapter")
fun RecyclerView.setAdapter(userViewerAdapter: UserViewerAdapter) {
    adapter = userViewerAdapter
}