package com.sergey.mvvm.model.repository

import android.util.Log
import com.sergey.mvvm.model.repository.db.Post
import com.sergey.mvvm.model.repository.db.PostDAO
import com.sergey.mvvm.model.repository.network.PostService

class PostRepository(
    private val dao: PostDAO,
    private val postService: PostService
) {
    val posts = dao.getAll()

    suspend fun getAll() {
        val count = dao.getDataCount()
        Log.e("PostRepository", "dao.getDataCount() = $count")
        if (count == 0) {
            try {
                dao.insertAll(postService.getAll())
                Log.e("PostService", "getAll()")
            } catch (e: Exception) {
            }
        }
    }

    suspend fun search(title: String): List<Post> {
        return dao.searchByTitle(title)
    }

    suspend fun insert(post: Post) {
        try {
            dao.insert(post)
            postService.insert(toMyPost(post))
        } catch (e: Exception) {
            //dao.insert(post)
        }
    }

    suspend fun update(post: Post) {
        try {
            dao.update(postService.update(post.id, post))
        } catch (e: Exception) {
            dao.update(post)
        }
    }

    suspend fun delete(post: Post) {
        try {
            postService.delete(post.id)
            dao.delete(post)
        } catch (e: Exception) {
            dao.delete(post)
        }
    }

    suspend fun refresh() {
        try {
            dao.refresh(postService.getAll())
            Log.e("PostService", "getAll()")
        } catch (e: Exception) {
            dao.deleteAll()
        }
    }

}