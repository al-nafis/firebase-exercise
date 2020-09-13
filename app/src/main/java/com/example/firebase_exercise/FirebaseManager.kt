package com.example.firebase_exercise

import android.util.Log
import com.example.firebase_exercise.dagger_components.UserDatabase
import com.google.firebase.database.*
import javax.inject.Inject

class FirebaseManager @Inject constructor(@UserDatabase private val databaseReference: DatabaseReference) {

    fun onCreate() {
        addData()
    }

    private val user = User().apply {
        name = "Bristi"
        age = 31
        sex = "Female"
    }

    fun addData() {
        databaseReference.child(user.name).setValue(user)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("ZARAH", "Failed to read value.", error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val t: GenericTypeIndicator<HashMap<String, User>> = object : GenericTypeIndicator<HashMap<String, User>>() {}
                val map: HashMap<String, User> = snapshot.getValue(t) as HashMap<String, User>
                Log.d("ZARAH", "Value is: " + map["Zarah"]?.name)
            }

        })
    }

//    fun getUser(): User {
//        databaseReference
//        return User("dasda",1, "dasd")
//    }
}