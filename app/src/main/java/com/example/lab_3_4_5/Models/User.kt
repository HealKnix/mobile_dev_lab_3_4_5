package com.example.lab_3_4_5.Models

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class User(
    var id: Int = -1,
    var login: String = "",
    var email: String = "",
    var password: String = ""
) {
    companion object {
        private val firebaseRef = FirebaseDatabase.getInstance().getReference("Users")
        val userList = mutableListOf<User>()
        private var currentUser: User? = null

        fun setCurrentUser(user: User?) {
            currentUser = user
        }

        fun getCurrentUser(): User? {
            return currentUser
        }

        fun fetchUserData(callback: (MutableList<User>) -> Unit) {
            firebaseRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnap in snapshot.children) {
                            val user = userSnap.getValue(User::class.java)!!
                            userList.add(user)
                        }
                    }
                    callback(userList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("sign_up", "Failed to read value.", error.toException())
                    callback(mutableListOf<User>()) // передаем пустой список в случае ошибки
                }
            })
        }

    }
}
