package com.example.lab_3_4_5.Models

import com.google.gson.annotations.SerializedName

data class Place (
    @SerializedName("id") var id: Int = -1,
    @SerializedName("parentId") var parentId: Int? = -1,
    @SerializedName("name") var name: String = "",
    @SerializedName("areas") var areas: List<Place> = listOf()
)