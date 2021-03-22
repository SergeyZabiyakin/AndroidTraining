package com.example.hw10

import android.annotation.SuppressLint
import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private lateinit var progress: ProgressBar
    private lateinit var editText: EditText

    enum class Action {
        SEARCH, SEND;
    }

    private var action = Action.SEND

    private var binder: LocalService.LocalBinder? = null

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            binder = service as LocalService.LocalBinder
            binder?.apply {
                registerCallback(progress, recycler)
                requestPosts()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            binder = null
        }

    }

    override fun onStart() {
        super.onStart()
        Intent(this, LocalService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress = findViewById(R.id.progressBar)
        recycler = findViewById(R.id.myRecyclerView)

        recycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        editText = findViewById(R.id.editText)
        editText.setOnEditorActionListener { v, _, _ ->
            return@setOnEditorActionListener when (action) {
                Action.SEND -> {
                    if (!(v.text.isBlank() || v.text.isEmpty())) {
                        binder?.addPost(v.text.toString())
                        v.text = ""
                    }
                    false
                }
                Action.SEARCH -> {
                    if (!(v.text.isBlank() || v.text.isEmpty())) {
                        //
                        v.text = ""
                    }
                    false
                }
            }
        }

        findViewById<ImageButton>(R.id.buttonRefresh).setOnClickListener {
            binder?.refresh()
        }

        findViewById<ImageButton>(R.id.buttonUp).setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                action = Action.SEND
               /* editText.setImeActionLabel("SEND", EditorInfo.IME_ACTION_SEND)
                editText.imeOptions = EditorInfo.IME_ACTION_SEND*/
            }
            false
        }

        findViewById<ImageButton>(R.id.buttonSearch).setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                action = Action.SEARCH
                 /*editText.setImeActionLabel("SEARCH", EditorInfo.IME_ACTION_SEARCH)
                 editText.imeOptions = EditorInfo.IME_ACTION_SEARCH*/
            }
            false
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        action = savedInstanceState.getSerializable("ACTION") as Action
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("ACTION", action)
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        binder?.clear()
        unbindService(connection)
        super.onStop()
    }

}