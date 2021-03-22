package com.example.hw10

import retrofit2.Response
import retrofit2.http.*

interface GuideService {
    @GET("posts")
    suspend fun get(): List<Post>

    @DELETE("posts/{id}")
    suspend fun delete(@Path("id") id: Long)

    @POST("posts")
    @Headers("Content-type:application/json;charset=UTF-8 ")
    suspend fun post(@Body post: MyPost) : Post
}