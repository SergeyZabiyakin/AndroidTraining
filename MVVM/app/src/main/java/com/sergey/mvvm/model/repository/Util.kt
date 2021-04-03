package com.sergey.mvvm.model.repository

import com.sergey.mvvm.model.repository.db.Post
import com.sergey.mvvm.model.repository.network.MyPost

fun toPost(id: Int, p: MyPost): Post {
    return Post(id, p.userId, p.title, p.body)
}

fun toMyPost(p: Post): MyPost {
    return MyPost(p.userId, p.title, p.body)
}

