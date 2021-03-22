package com.example.hw8

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var binder: LocalService.LocalBinder? = null

    private val mistake = Mistake()
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            Log.e("MainActivity", "onServiceConnected")
            binder = service as LocalService.LocalBinder
            binder?.apply {
                registerCallback(progressBar, myRecyclerView, mistake)
                requestPosts()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            binder = null
        }

    }

    override fun onStart() {
        super.onStart()
        Log.e("MainActivity", "onStart")
        Intent(this, LocalService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("MainActivity", "onCreate")

        progressBar = findViewById(R.id.progressBar)
        if (savedInstanceState != null) {
            progressBar.visibility = ProgressBar.INVISIBLE
        }
        myRecyclerView = findViewById(R.id.myRecyclerView)

        myRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            //adapter = LocalService.adapter
        }

        findViewById<EditText>(R.id.editText).setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    if (!(v.text.isBlank() || v.text.isEmpty())) {
                        binder?.addPost(v.text.toString())
                        v.text = ""
                    }

                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    true
                }
                else -> false
            }
        }

        findViewById<ImageButton>(R.id.buttonRefresh).setOnClickListener {
            binder?.refresh()
        }
    }

    override fun onStop() {
        Log.e("MainActivity", "onStop")
        binder?.clear()
        unbindService(connection)
        super.onStop()
    }

    override fun onDestroy() {
        Log.e("MainActivity", "onDestroy")
        super.onDestroy()
    }

    inner class Mistake {
        fun problems(code: Int) {
            when (code / 100) {
                3 -> {
                    makeToast("Fuck off")
                }
                4 -> {
                    makeToast("Fuck you")
                }
                5 -> {
                    makeToast("Fuck")
                }
                else -> {
                    makeToast("( -_-)(I_I)(-_- )")
                }
            }
        }

        fun makeToast(text: String) {
            Toast.makeText(
                this@MainActivity,
                text,
                Toast.LENGTH_LONG
            ).show()
        }
    }

}