package com.sergey.mvvm.repository.network

import com.sergey.mvvm.repository.db.Post
import retrofit2.http.*
import java.util.*

interface PostService {


    @GET("posts")
    suspend fun getAll(): List<Post>

    @DELETE("posts/{id}")
    suspend fun delete(@Path("id") id: Int)

    @POST("posts")
    @Headers("Content-type:application/json;charset=UTF-8 ")
    suspend fun insert(@Body post: MyPost): Post

    @PUT("posts/{id}")
    @Headers("Content-type:application/json;charset=UTF-8 ")
    suspend fun update(
        @Path("id") id: Int,
        @Body post: Post
    ): Post

}