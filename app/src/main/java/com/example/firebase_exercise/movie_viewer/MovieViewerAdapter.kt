package com.example.firebase_exercise.movie_viewer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase_exercise.data.Movie
import com.example.firebase_exercise.databinding.MovieInfoItemViewBinding
import javax.inject.Inject

class MovieViewerAdapter @Inject constructor() : RecyclerView.Adapter<MovieViewerAdapter.MovieViewerItemHolder>() {

    private var movieList = listOf<Movie>()

    fun setMovies(list: List<Movie>) {
        movieList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewerItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = MovieInfoItemViewBinding.inflate(inflater, parent, false)
        return MovieViewerItemHolder(itemBinding)
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: MovieViewerItemHolder, position: Int) { holder.view.movie = movieList[position] }

    inner class MovieViewerItemHolder(val view: MovieInfoItemViewBinding) : RecyclerView.ViewHolder(view.root)
}