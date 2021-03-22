package com.example.hw10

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.IOException


class LocalService : Service() {

    private val adapter = UserAdapter(mutableListOf()) {
        it.deleted = true
        Log.e("LocalService", "Posts deleted !")
        scope.launch {
            try {
                MyApp.postDao.update(it)
                MyApp.guideService.delete(it.id)
            } catch (e: Exception) {
                problems(e)
            }
        }
    }

    lateinit var scope: CoroutineScope
    private var receiver: RecyclerView? = null
    private var progress: ProgressBar? = null

    inner class LocalBinder : Binder() {

        fun registerCallback(
            progress: ProgressBar,
            recyclerView: RecyclerView,
        ) {
            this@LocalService.progress = progress
            receiver = recyclerView
            receiver?.adapter = adapter
        }

        fun requestPosts() {
            if (adapter.items.isNotEmpty()) {
                progress?.visibility = ProgressBar.INVISIBLE
            }
        }

        fun addPost(str: String) {
            val post = Post(
                adapter.items.size + 1.toLong(),
                9032001,
                "Новый",
                str
            )

            scope.launch {
                try {
                    MyApp.postDao.insert(post)
                    MyApp.guideService.post(MyPost(9032001, "Новый", str))
                } catch (e: Exception) {
                    problems(e)
                }
            }

            adapter.items.add(post)
            adapter.notifyItemInserted(adapter.items.size - 1)
            receiver?.scrollToPosition(adapter.items.size - 1)
        }

        fun refresh() {
            scope.launch {
                try {
                    progress?.visibility = ProgressBar.VISIBLE
                    val posts = MyApp.guideService.get()
                    MyApp.postDao.deleteAll()
                    MyApp.postDao.insertAll(posts)
                    adapter.apply {
                        items.clear()
                        items.addAll(posts)
                        notifyDataSetChanged()
                    }
                    Log.e("LocalService", "Refresh all posts !")
                } catch (e: Exception) {
                    problems(e)
                } finally {
                    progress?.visibility = ProgressBar.INVISIBLE
                }
            }
        }

        fun clear() {
            receiver = null
            progress = null
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return LocalBinder()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            var posts = MyApp.postDao.getAll()
            if (posts.isNotEmpty()) {
                progress?.visibility = ProgressBar.INVISIBLE
                adapter.items.addAll(posts)
                adapter.notifyDataSetChanged()
            } else {
                scope.launch {
                    try {
                        posts = MyApp.guideService.get()
                        adapter.items.addAll(posts)
                        adapter.notifyDataSetChanged()
                        progress?.visibility = ProgressBar.INVISIBLE
                        MyApp.postDao.insertAll(posts)
                        Log.e("LocalService", "Get all posts !")
                    } catch (e: Exception) {
                        problems(e)
                    }
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun problems(e: Exception) {
        Log.e("API", "Failed with", e)
        Toast.makeText(
            this,
            "Сonnection issues !",
            Toast.LENGTH_LONG
        ).show()
    }

/*private inner class Callback<T>(
    response: Response<T>,
    handler: (Response<T>) -> Unit
) {
    init {
        if (response.isSuccessful) {
            if (response.body() != null) {
                handler(response)
            } else {
                Log.e("LocalService", "Response.body() == null")
            }
        } else {
            //problems(response.code())
        }
    }
}*/


/*private fun problems(code: Int) {
    when (code / 100) {
        3 -> {
            makeToast("Fuck off")
        }
        4 -> {
            makeToast("Fuck you")
        }
        5 -> {
            makeToast("Fuck")
        }
        *//*else -> {
                makeToast("( -_-)(I_I)(-_- )")
            }*//*
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(
            this,
            text,
            Toast.LENGTH_LONG
        ).show()
    }*/

    override fun onDestroy() {
        Toast.makeText(
            this,
            "Destroy service !",
            Toast.LENGTH_LONG
        ).show()
        scope.cancel()
        super.onDestroy()
    }
}