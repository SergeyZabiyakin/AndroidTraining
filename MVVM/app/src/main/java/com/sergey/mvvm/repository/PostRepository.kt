package com.sergey.mvvm.repository

import androidx.lifecycle.LiveData
import com.sergey.mvvm.repository.db.Post
import com.sergey.mvvm.repository.db.PostDAO
import com.sergey.mvvm.repository.network.PostService

class PostRepository(
    private val dao: PostDAO,
    private val postService: PostService
) {
    /*val posts = dao.getAll()
    val countPost = dao.getDataCount()*/

    suspend fun getAll(): LiveData<List<Post>> {
        val posts = dao.getAll()
        if (posts.value.isNullOrEmpty()) {
            try {
                dao.insertAll(postService.getAll())
            } catch (e: Exception) {
            }
        }
        return posts
    }

    suspend fun insert(post: Post) {
        try {
            dao.insert(postService.insert(toMyPost(post)))
        } catch (e: Exception) {
            dao.insert(post)
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
        } catch (e: Exception) {
            dao.deleteAll()
        }
    }

}