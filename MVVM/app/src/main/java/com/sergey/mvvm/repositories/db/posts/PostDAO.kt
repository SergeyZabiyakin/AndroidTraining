package com.sergey.androidarchitecturecomponents.repositories.db.posts

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PostDAO {
   /* @Query("SELECT * FROM posts_data_table WHERE id = :id")
    suspend fun getByIdPost(id: Long): Post*/

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
}