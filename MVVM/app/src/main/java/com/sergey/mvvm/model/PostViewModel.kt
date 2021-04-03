package com.sergey.mvvm.model

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergey.mvvm.model.repository.PostRepository
import com.sergey.mvvm.model.repository.db.Post
import kotlinx.coroutines.launch

class PostViewModel(private val repository: PostRepository) : ViewModel(), Observable {

    val posts = repository.posts

    @Bindable
    val editTextHint = MutableLiveData<String>()

    @Bindable
    val editTextData = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            repository.getAll()
        }
        editTextHint.value = "Insert"
        editTextData.value = ""
    }

    fun insertOrUpdateOrSearch(post: Post) {
        if (!editTextData.value.isNullOrBlank()) {
            when (editTextHint.value) {
                "Insert" -> {
                    insert(post)
                }
                "Update" -> {
                    update(post)
                }
                "Search" -> {
                    search(post.body)
                }
            }
            setHintInsert()
            editTextData.value = ""
        } else {
            editTextData.value = "The text cannot be empty !"
        }
    }

    fun setHintSearch() {
        editTextHint.value = "Search"
    }

    fun setHintInsert() {
        editTextHint.value = "Insert"
    }

    fun setHintUpdate() {
        editTextHint.value = "Update"
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

    fun search(title: String) = viewModelScope.launch {
        //return repository.search(title)
    }

    fun refresh() = viewModelScope.launch {
        repository.refresh()
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

}