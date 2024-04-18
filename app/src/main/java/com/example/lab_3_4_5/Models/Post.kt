package com.example.lab_3_4_5.Models

data class Post(
    var id: Int,
    var title: String,
    var text: String,
    var createdBy: String,
    var likes: Int,
    var isLiked: Boolean
)