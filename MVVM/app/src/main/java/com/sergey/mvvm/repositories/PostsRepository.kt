package com.sergey.androidarchitecturecomponents.repositories

import com.sergey.androidarchitecturecomponents.repositories.db.posts.PostDAO
import com.sergey.androidarchitecturecomponents.repositories.retrofit.posts.FakeAPI

class PostsRepository(
    private val dao: PostDAO,
    private val fakeAPI: FakeAPI
) {
    val posts = dao.getAll()


}