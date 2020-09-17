package com.example.firebase_exercise.regular_database.viewer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase_exercise.data.User
import com.example.firebase_exercise.databinding.UserInfoItemViewBinding
import javax.inject.Inject

class UserViewerAdapter @Inject constructor() : RecyclerView.Adapter<UserViewerAdapter.UserViewerItemHolder>() {

    private var userList = listOf<User>()

    fun setUsers(list: List<User>) {
        userList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewerItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = UserInfoItemViewBinding.inflate(inflater, parent, false)
        return UserViewerItemHolder(itemBinding)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: UserViewerItemHolder, position: Int) { holder.view.user = userList[position] }

    inner class UserViewerItemHolder(val view: UserInfoItemViewBinding) : RecyclerView.ViewHolder(view.root)
}