package com.sergey.androidarchitecturecomponents.repositories.retrofit.posts

import com.sergey.androidarchitecturecomponents.repositories.db.posts.Post
import retrofit2.http.*

interface FakeAPI {
    @GET("posts")
    suspend fun getAll(): List<Post>

    @DELETE("posts/{id}")
    suspend fun delete(@Path("id") id: Long)

    @POST("posts")
    @Headers("Content-type:application/json;charset=UTF-8 ")
    suspend fun push(@Body post: MyPost): Post

    @PUT("posts/{id}")
    @Headers("Content-type:application/json;charset=UTF-8 ")
    suspend fun update(
        @Path("id") id: Long,
        @Body post: MyPost
    ): Post
}