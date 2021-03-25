package com.sergey.mvvm

import android.app.Application
import com.sergey.mvvm.repository.PostRepository
import com.sergey.mvvm.repository.db.PostDatabase
import com.sergey.mvvm.repository.network.PostRetrofit
import com.sergey.mvvm.repository.network.PostService


class App : Application() {

    companion object {
        lateinit var repository: PostRepository
    }

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
