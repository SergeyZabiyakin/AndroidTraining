package com.sergey.mvvm.repository

import com.sergey.mvvm.repository.db.Post
import com.sergey.mvvm.repository.network.MyPost

fun toPost(id: Int, p: MyPost): Post {
    return Post(id, p.userId, p.title, p.body)
}

fun toMyPost(p: Post): MyPost {
    return MyPost(p.userId, p.title, p.body)
}

