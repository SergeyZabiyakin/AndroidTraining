package com.sergey.mvvm.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface PostDAO {


    @Insert
    suspend fun insert(post: Post)

    @Insert
    suspend fun insertAll(posts: List<Post>)

    @Update
    suspend fun update(post: Post)

    @Delete
    suspend fun delete(post: Post)

    @Query("DELETE FROM posts_data_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM posts_data_table")
    fun getAll(): LiveData<List<Post>>

    @Query("SELECT * FROM posts_data_table WHERE title LIKE :title")
    suspend fun searchByTitle(title: String): List<Post>

    @Query("SELECT COUNT(*) FROM posts_data_table")
    suspend fun getDataCount(): Int

    @Transaction
    suspend fun refresh(posts: List<Post>) {
        deleteAll()
        insertAll(posts)
    }
}