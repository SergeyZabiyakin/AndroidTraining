package com.example.skynet

import android.app.Application
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MyApp : Application() {

    companion object {
        lateinit var instance: MyApp
            private set
    }

    lateinit var guideService: GuideService
    lateinit var posts: MutableList<Post>

    override fun onCreate() {
        super.onCreate()
        instance = this
        val retrofit =
            Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        guideService = retrofit.create(GuideService::class.java)
    }

}
