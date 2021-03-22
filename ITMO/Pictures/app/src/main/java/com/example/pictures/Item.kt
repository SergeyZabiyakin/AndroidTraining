package com.example.pictures

import android.graphics.Bitmap

class Item(var sizes: Array<Image>) {

    fun init() {
        sizes.map { it.init() }
        sizes.sortWith(Comparator { i1: Image, i2: Image -> i1.pixels - i2.pixels })
    }

    fun getMinImage(): String {
        return sizes[1].url
    }
}