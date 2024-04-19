package com.example.lab_3_4_5.Models

data class Post(
    var id: Int = -1,
    var title: String = "",
    var text: String = "",
    var createdBy: Int = -1,
    var likes: Int = 0,
    var isLiked: Boolean = false
)
