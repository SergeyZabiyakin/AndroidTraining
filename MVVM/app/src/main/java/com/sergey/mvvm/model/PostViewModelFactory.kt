package com.sergey.mvvm.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sergey.mvvm.model.repository.PostRepository

class PostViewModelFactory(private val repository: PostRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            Log.e("PostViewModelFactory" , "Create PostViewModel")
            return PostViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}