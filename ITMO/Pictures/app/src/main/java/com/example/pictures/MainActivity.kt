package com.example.pictures

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.LruCache
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL


class MainActivity : AppCompatActivity() {
    companion object {
        const val BIG_IMAGE = "BIG_IMAGE"
        private const val ITEM_LIST = "ITEM_LIST"
        private const val VK_GET =
            "https://api.vk.com/method/photos.search?count=100&radius=6000&" +
                    "access_token=45ac00be45ac00be45ac00befd45d85bc7445ac45ac00be1a2f43d347907c3d3710938d&v=5.1244"
        private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        // Use 1/8th of the available memory for this memory cache.
        private val cacheSize = maxMemory / 8
        private val memoryCache: LruCache<String, Bitmap> =
            object : LruCache<String, Bitmap>(cacheSize) {
                override fun sizeOf(key: String, bitmap: Bitmap): Int {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.byteCount / 1024
                }
            }
    }

    private lateinit var itemList: List<Item>
    private val myTask = DownloadVKItemList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            myTask.execute(VK_GET)
        } else {
            itemList = Gson().fromJson(
                savedInstanceState.getString(ITEM_LIST),
                object : TypeToken<List<Item>>() {}.type
            )
            init()
        }
    }

    private fun init() {
        val viewManaher = GridLayoutManager(this@MainActivity, 2)
        myRecyclerView.apply {
            layoutManager = viewManaher
            adapter = UserAdapter(memoryCache, itemList) {
                startActivity(
                    Intent(this@MainActivity, ImageActivity::class.java)
                )
                startService(
                    Intent(this@MainActivity, MyService::class.java).putExtra(
                        BIG_IMAGE,
                        it.sizes.last().url
                    )
                )
            }
        }
        myRecyclerView.setHasFixedSize(true)
    }

    @SuppressLint("StaticFieldLeak")
    private inner class DownloadVKItemList : AsyncTask<String, Unit, Unit>() {
        override fun doInBackground(vararg params: String): Unit {
            itemList = Gson().fromJson(URL(params[0]).openConnection().run {
                connect()
                getInputStream().bufferedReader().readLines().joinToString("")
            }, ResponseVK::class.java).response.items.toList()

            itemList.map {
                it.init()
                memoryCache.put(
                    it.getMinImage(),
                    BitmapFactory.decodeStream(URL(it.getMinImage()).openStream())
                )
            }
        }

        override fun onPostExecute(result: Unit?) {
            init()
            super.onPostExecute(result)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(
            ITEM_LIST, Gson().toJson(itemList)
        )
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        if (myTask.getStatus() === AsyncTask.Status.RUNNING) {
            myTask.cancel(true)
        }
        super.onDestroy()
    }
}