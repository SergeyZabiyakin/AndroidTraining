package com.example.hw10

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MyApp : Application() {

    companion object {
        lateinit var guideService: GuideService
        lateinit var postDao: PostDao
    }

    override fun onCreate() {
        super.onCreate()

        Intent(this, LocalService::class.java).also { intent ->
            startService(intent)
        }

        postDao = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "MyDataBase"
        ).build().postDao()

        val retrofit =
            Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        guideService = retrofit.create(GuideService::class.java)
    }
}
