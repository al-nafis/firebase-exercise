package com.example.firebase_exercise

import android.util.Log
import com.example.firebase_exercise.dagger_components.UserDatabase
import com.example.firebase_exercise.regular_database.UpdateCurrentUser
import com.google.firebase.database.*
import javax.inject.Inject

class FirebaseManager @Inject constructor(@UserDatabase private val databaseReference: DatabaseReference) {

    fun listenToUsers(updateCurrentUser: UpdateCurrentUser) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("ZARAH", error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val t: GenericTypeIndicator<HashMap<String, User>> =
                        object : GenericTypeIndicator<HashMap<String, User>>() {}
                    val map: HashMap<String, User> = snapshot.getValue(t) as HashMap<String, User>
                    val list = mutableListOf<User>()
                    map.forEach { user -> list.add(user.value) }
                    updateCurrentUser.updateUserInfo(list)
                }
            }
        })
    }

    fun addUser(user: User) {
        val idRef = user.email.replace(Regex("[.#$\\[\\]]"), "")
        databaseReference.child(idRef).setValue(user)
    }
}