package com.sergey.androidarchitecturecomponents.repositories.db.posts

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Post::class], version = 1)
abstract class PostsDatabase : RoomDatabase() {
    abstract val postDAO: PostDAO

    companion object {
        @Volatile
        private var INSTANCE: PostsDatabase? = null

        fun getInstance(context: Context): PostsDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PostsDatabase::class.java,
                        "post_data_database"
                    ).build()
                }
                return instance
            }
        }
    }
}