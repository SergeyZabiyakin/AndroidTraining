package com.sergey.mvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sergey.mvvm.App
import com.sergey.mvvm.R
import com.sergey.mvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var postViewModel: PostViewModel
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val factory = PostViewModelFactory(App.repository)
        postViewModel = ViewModelProvider(this, factory).get(PostViewModel::class.java)
        binding.postViewModel = postViewModel
        binding.lifecycleOwner = this

        binding.buttonRefresh.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            postViewModel.refresh()
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter { }
        binding.recyclerView.adapter = adapter
        postViewModel.posts.observe(this, {
            binding.progressBar.visibility = View.INVISIBLE
            adapter.setItems(it)
        })
    }
}