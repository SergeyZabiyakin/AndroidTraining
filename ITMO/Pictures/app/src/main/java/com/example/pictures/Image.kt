package com.example.pictures

class Image(
    val url: String,
    val width: Int,
    val height: Int
) {
    var pixels: Int = 0
    fun init() {
        pixels = width * height
    }
}