package com.example.lab_3_4_5.Models

data class Post(
    var id: Int = -1,
    var title: String = "",
    var text: String = "",
    var createdByUserId: Int = -1,
    var createdByUserName: String = "",
    val likedByUsers: MutableList<Int> = mutableListOf<Int>(),
    var likes: Int = 0,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
