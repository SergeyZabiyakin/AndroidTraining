package com.example.hw8

import retrofit2.Call
import retrofit2.http.*

interface GuideService {
    @GET("posts")
    fun get(): Call<List<Post>>

    @DELETE("posts/{id}")
    fun delete(@Path("id") id: Long): Call<Unit>

    @POST("posts")
    @Headers("Content-type:application/json;charset=UTF-8 ")
    fun post(@Body post: MyPost) : Call<Post>
}