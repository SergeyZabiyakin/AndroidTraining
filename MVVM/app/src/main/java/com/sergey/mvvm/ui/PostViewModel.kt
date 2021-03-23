package com.sergey.mvvm.ui

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergey.mvvm.repository.PostRepository
import com.sergey.mvvm.repository.db.Post
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PostViewModel(private val repository: PostRepository) : ViewModel() {

    lateinit var posts: LiveData<List<Post>>

    init {
        viewModelScope.launch {
           // posts = repository.getAll()
        }
    }

    @Bindable
    val saveOrUpdateEditText = MutableLiveData<String>()

    init {
        saveOrUpdateEditText.value = "Save"
    }

    fun saveOrUpdate() {

    }

    fun insert(post: Post) = viewModelScope.launch {
        repository.insert(post)
    }

    fun update(post: Post) = viewModelScope.launch {
        repository.update(post)
    }

    fun delete(post: Post) = viewModelScope.launch {
        repository.delete(post)
    }

    fun refresh() = viewModelScope.launch {
        repository.refresh()
    }

}