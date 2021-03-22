package com.example.hw8

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.AsyncTask
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LocalService : Service() {

    companion object {
        val adapter = UserAdapter(mutableListOf()) {
            it.deleted = true

            DoAsync {
                MyApp.postDao.update(it)
            }

            MyApp.guideService.delete(it.id)
                .enqueue(MyCallback(mistake) { Log.e("LocalService", "Post deleted !") })

        }
        private var mistake: MainActivity.Mistake? = null
    }

    private var receiver: RecyclerView? = null
    private var progressBar: ProgressBar? = null


    inner class LocalBinder : Binder() {

        fun registerCallback(
            progress: ProgressBar,
            recyclerView: RecyclerView,
            mistakeAc: MainActivity.Mistake
        ) {
            mistake = mistakeAc
            progressBar = progress
            receiver = recyclerView
            receiver?.adapter = adapter
        }

        fun requestPosts() {
            if (adapter.items.isEmpty()) {
                GetAllPostsAsync().execute()
            }
        }

        fun addPost(str: String) {
            val post = Post(
                adapter.items.size + 1.toLong(),
                9032001,
                "Новый",
                str
            )

            DoAsync {
                MyApp.postDao.insert(post)
            }

            adapter.items.add(post)
            adapter.notifyItemInserted(adapter.items.size - 1)
            receiver?.scrollToPosition(adapter.items.size - 1)

            MyApp.guideService.post(MyPost(9032001, "Новый", str))
                .enqueue(MyCallback<Post>(mistake) { Log.e("LocalService", "Post added !") })
        }

        fun refresh() {
            MyApp.guideService.get().enqueue(MyCallback(mistake) {
                Log.e("LocalService", "Refresh all posts !")
                val posts = it.body()!!
                DoAsync {
                    MyApp.postDao.deleteAll()
                    MyApp.postDao.insertAll(posts)
                }
                adapter.apply {
                    items.clear()
                    items.addAll(posts)
                    notifyDataSetChanged()
                }
            })
        }

        fun clear() {
            receiver = null
            progressBar = null
            mistake = null
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return LocalBinder()
    }


    private class DoAsync(private val handler: () -> Unit) : AsyncTask<Unit, Unit, Unit>() {
        init {
            execute()
        }

        override fun doInBackground(vararg params: Unit) {
            handler()
        }
    }


    @SuppressLint("StaticFieldLeak")
    private inner class GetAllPostsAsync() : AsyncTask<Unit, Unit, List<Post>>() {

        override fun doInBackground(vararg params: Unit): List<Post> {
            return MyApp.postDao.getAll()
        }

        override fun onPostExecute(result: List<Post>) {
            if (result.isNotEmpty()) {
                progressBar?.visibility = ProgressBar.INVISIBLE
                adapter.items.addAll(result)
                adapter.notifyDataSetChanged()
            } else {
                MyApp.guideService.get().enqueue(MyCallback<List<Post>>(mistake) {
                    Log.e("LocalService", "Get all posts !")
                    val posts = it.body()!!
                    adapter.items.addAll(posts)
                    adapter.notifyDataSetChanged()
                    progressBar?.visibility = ProgressBar.INVISIBLE
                    DoAsync {
                        MyApp.postDao.insertAll(posts)
                    }
                })
            }
            super.onPostExecute(result)
        }
    }

    private class MyCallback<T>(
        private var mis: MainActivity.Mistake?,
        private val handler: (response: Response<T>) -> Unit
    ) :
        Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                if (response.body() != null) {
                    handler(response)
                } else {
                    Log.e("LocalService", "Response.body() == null")
                }
            } else {
                mis?.problems(response.code())
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.e("Guide API", "Failed with", t)
        }
    }

}