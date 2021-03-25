package com.sergey.mvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sergey.mvvm.App
import com.sergey.mvvm.R
import com.sergey.mvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var postViewModel: PostViewModel

    companion object {
        private val factory = PostViewModelFactory(App.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        postViewModel = ViewModelProvider(this, factory).get(PostViewModel::class.java)
        binding.postViewModel = postViewModel
        binding.lifecycleOwner = this
    }
}