package com.example.pictures

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.util.LruCache
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_image.*
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.nio.ByteBuffer


class ImageActivity : AppCompatActivity() {

    companion object {
        private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        private val cacheSize = maxMemory / 8
        val memoryCache: LruCache<String, Bitmap> =
            object : LruCache<String, Bitmap>(cacheSize) {
                override fun sizeOf(key: String, bitmap: Bitmap): Int {
                    return bitmap.byteCount / 1024
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        big_image.setOnClickListener {
            memoryCache.remove(MainActivity.BIG_IMAGE)
            finish()
            /*startActivity(Intent(this@ImageActivity, MainActivity::class.java))*/
        }
        val intentFilter = IntentFilter(MyService.ACTION_MICROSERVICE)
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(MyBroadcastReceiver(), intentFilter)
    }

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            image.setImageBitmap(memoryCache.get(MainActivity.BIG_IMAGE))
            progress_bar.visibility = ProgressBar.INVISIBLE
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (memoryCache.get(MainActivity.BIG_IMAGE)!=null) {
            image.setImageBitmap(memoryCache.get(MainActivity.BIG_IMAGE))
            progress_bar.visibility = ProgressBar.INVISIBLE
        }
        super.onRestoreInstanceState(savedInstanceState)
    }
}