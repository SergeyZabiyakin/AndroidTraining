package com.sergey.mvvm

import android.app.Application
import com.sergey.mvvm.model.repository.PostRepository
import com.sergey.mvvm.model.repository.db.PostDatabase
import com.sergey.mvvm.model.repository.network.PostRetrofit
import com.sergey.mvvm.model.repository.network.PostService


class App : Application() {

    lateinit var repository: PostRepository

    override fun onCreate() {
        super.onCreate()
        val db = PostDatabase.getInstance(this)
        val network = PostRetrofit.getInstance()
        repository = PostRepository(
            db.postDAO,
            network.create(PostService::class.java)
        )

    }
}
