package com.example.pictures

import android.app.IntentService
import android.content.Intent
import android.graphics.BitmapFactory
import java.net.URL

class MyService : IntentService("MyService") {
    companion object {
        const val ACTION_MICROSERVICE = "ACTION_MICROSERVICE"
    }

    override fun onHandleIntent(intent: Intent?) {
        val url = intent?.getStringExtra(MainActivity.BIG_IMAGE)
        ImageActivity.memoryCache.put(
            MainActivity.BIG_IMAGE, BitmapFactory.decodeStream(
                URL(url).openConnection().getInputStream()
            )
        )
        val respondent = Intent()
        respondent.action = ACTION_MICROSERVICE
        respondent.addCategory(Intent.CATEGORY_DEFAULT)
        sendBroadcast(respondent)
    }
}