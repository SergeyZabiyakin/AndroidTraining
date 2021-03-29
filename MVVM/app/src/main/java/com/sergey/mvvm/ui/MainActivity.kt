package com.sergey.mvvm.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergey.mvvm.App
import com.sergey.mvvm.R
import com.sergey.mvvm.databinding.ActivityMainBinding
import com.sergey.mvvm.model.PostViewModel
import com.sergey.mvvm.model.PostViewModelFactory
import com.sergey.mvvm.repository.db.Post

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var postViewModel: PostViewModel
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var editText: EditText

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val factory = PostViewModelFactory(App.repository)
        postViewModel = ViewModelProvider(this, factory).get(PostViewModel::class.java)
        binding.postViewModel = postViewModel
        binding.lifecycleOwner = this

        binding.refresh.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            postViewModel.refresh()
        }

        editText = binding.editText

        editText.setOnEditorActionListener { v, actionId, event ->
            /*postViewModel.insertOrUpdateOrSearch(
                Post(
                    postViewModel.posts.value!!.size,
                    -1,
                    "Новое",
                    editText.text.toString()
                )
            )*/
            return@setOnEditorActionListener true
        }

        binding.search.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                postViewModel.setHintSearch()
            }
            false
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter {
            postViewModel.delete(it)
        }
        binding.recyclerView.adapter = adapter
        postViewModel.posts.observe(this, {
            binding.progressBar.visibility = View.INVISIBLE
            adapter.setItems(it)
        })
    }
}