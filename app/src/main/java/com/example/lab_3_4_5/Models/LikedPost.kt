package com.example.lab_3_4_5.Models

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class LikedPost(
    val id: Int = -1,
    val postId: Int = -1,
    val userId: Int = -1,
    val isLiked: Boolean = true
) {
    companion object {
        private val firebaseRef = FirebaseDatabase.getInstance().getReference("LikedPosts")

        fun fetchLikedPostData(callback: (MutableList<LikedPost>) -> Unit) {
            firebaseRef.addValueEventListener(object: ValueEventListener {
                val likedPostList = mutableListOf<LikedPost>()
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (likedPostSnap in snapshot.children) {
                            val likedPost = likedPostSnap.getValue(LikedPost::class.java)!!
                            likedPostList.add(likedPost)
                        }
                    }
                    callback(likedPostList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("sign_up", "Failed to read value.", error.toException())
                    callback(mutableListOf<LikedPost>()) // передаем пустой список в случае ошибки
                }
            })
        }
    }
}