package com.example.hw10

import androidx.room.*

@Dao
interface PostDao {
    @Query("SELECT * FROM post WHERE id = :id")
    suspend fun getById(id: Long): Post

    @Query("SELECT * FROM post")
    suspend fun getAll(): List<Post>

    @Insert
    suspend fun insertAll(posts: List<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post)

    @Update
    suspend fun update(post: Post)

    @Query("DELETE FROM post")
    suspend fun deleteAll()
}