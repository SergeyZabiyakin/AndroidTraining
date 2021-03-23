package com.sergey.mvvm.repository.db

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

    /*@Query("SELECT COUNT(*) FROM posts_data_table")
    fun getDataCount(): LiveData<Int>*/

    @Transaction
    suspend fun refresh(posts: List<Post>) {
        deleteAll()
        insertAll(posts)
    }
}