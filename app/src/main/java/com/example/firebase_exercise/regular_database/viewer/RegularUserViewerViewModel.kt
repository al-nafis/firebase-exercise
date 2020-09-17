package com.example.firebase_exercise.regular_database.viewer

import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.example.firebase_exercise.BaseViewModel
import com.example.firebase_exercise.data.User
import com.example.firebase_exercise.events.Toaster
import com.example.firebase_exercise.regular_database.FirebaseManager
import com.example.firebase_exercise.regular_database.UsersDataCallBack
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class RegularUserViewerViewModel @Inject constructor(
    private val manager: FirebaseManager,
    private val toaster: Toaster,
    val adapter: UserViewerAdapter
) : BaseViewModel() {

    val addUserScreenLaunchOrderChannel: PublishSubject<Boolean> = PublishSubject.create<Boolean>()

    private val listener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            toaster.toast(error.details)
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val t: GenericTypeIndicator<Map<String, User>> =
                    object : GenericTypeIndicator<Map<String, User>>() {}
                val map = snapshot.getValue(t) as Map<String, User>
                val users = mutableListOf<User>()
                for (user in map.values) {
                    users.add(user)
                }
                adapter.setUsers(users.sortedBy { it.name })
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        manager.listenToUserDataChange(listener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        manager.removeListener(listener)
    }

    fun addNewUser() = addUserScreenLaunchOrderChannel.onNext(true)
}
