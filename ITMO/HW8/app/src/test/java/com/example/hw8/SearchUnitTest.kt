package com.example.hw8

import androidx.room.Room
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val mySearch = mutableMapOf<String, Long>()
    private val posts =
        (1..10).map { Post(it.toLong(), it.toLong(), "Title : $it", "Body : $it", it % 2 == 0) }

    @Test
    fun addition_isCorrect() {
        //dB.insert(Post(10,20,"asdfsadf","sdasf"))
        //assertEquals(dB.getById(10), Post(10,20,"asdfsadf","sdasf"))
    }
}