package com.example.skynet

data class Post(
    val userId: Long,
    val id: Long,
    val title: String,
    val body: String,
    var deleted: Boolean = false
)