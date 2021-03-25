package com.sergey.mvvm.repository.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PostRetrofit {
    companion object{
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
        fun getInstance() : Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }
    }
}