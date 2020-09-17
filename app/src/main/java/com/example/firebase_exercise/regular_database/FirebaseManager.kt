package com.example.firebase_exercise.regular_database

import com.example.firebase_exercise.dagger_components.UserDatabase
import com.example.firebase_exercise.data.User
import com.google.firebase.database.*
import javax.inject.Inject

class FirebaseManager @Inject constructor(@UserDatabase private val databaseReference: DatabaseReference) {

    fun listenToUserDataChange(listener: ValueEventListener) =
        databaseReference.addValueEventListener(listener)

    fun removeListener(listener: ValueEventListener) =
        databaseReference.removeEventListener(listener)

    fun addUser(newUser: User, usersDataCallBack: UsersDataCallBack) {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                usersDataCallBack.onCancelled(error)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val t: GenericTypeIndicator<Map<String, User>> =
                        object : GenericTypeIndicator<Map<String, User>>() {}
                    val map = snapshot.getValue(t) as Map<String, User>
                    for (user in map.values) {
                        if (user.email == newUser.email) {
                            usersDataCallBack.userExists()
                            return
                        }
                    }
                }
                databaseReference.push().setValue(newUser)
                usersDataCallBack.onSuccessAddingNewUser()
            }
        })
    }
}