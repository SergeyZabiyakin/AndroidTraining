package com.example.skynet

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var myRecyclerView: RecyclerView

    private val callGet = MyApp.instance.guideService.get()

    private var initPosts = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myRecyclerView = findViewById(R.id.myRecyclerView)

        if(savedInstanceState == null || !savedInstanceState.getBoolean("INIT")) {
            Log.e("MainActivity", "callGet.enqueue")
            callGet.enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            MyApp.instance.posts = (response.body() as MutableList<Post>?)!!
                            init()
                        } else {
                            makeToast("Response.body() == null")
                        }
                    } else {
                        problems(response.code())
                    }
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    t.message?.let { makeToast(it) }
                }
            })
        } else {
            init()
        }

        findViewById<ImageButton>(R.id.buttonAdd).setOnClickListener {
            val editText = findViewById<EditText>(R.id.textCard)
            if (!(editText.text.isBlank() || editText.text.isEmpty())) {
                MyApp.instance.guideService.post(MyPost(9032001, "Новый", editText.text.toString()))
                    .enqueue(object : Callback<Post> {
                        override fun onResponse(call: Call<Post>, response: Response<Post>) {
                            if (response.isSuccessful) {
                                if (response.body() != null) {
                                    MyApp.instance.posts.add(response.body()!!)
                                    myRecyclerView.adapter?.notifyItemInserted(MyApp.instance.posts.size - 1)
                                    editText.text.clear()
                                } else {
                                    makeToast("Response.body() == null")
                                }
                            } else {
                                problems(response.code())
                            }
                        }

                        override fun onFailure(call: Call<Post>, t: Throwable) {
                            t.message?.let { makeToast(it) }
                        }
                    })
            }
        }
    }


    fun init() {

        initPosts = true

        val viewManaher = LinearLayoutManager(this@MainActivity)
        myRecyclerView.apply {
            layoutManager = viewManaher
            adapter = UserAdapter(MyApp.instance.posts) { post, holder ->
                // on Click
                MyApp.instance.guideService.delete(post.id).enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if (response.isSuccessful) {
                            post.deleted = true
                            adapter?.notifyItemChanged(holder.adapterPosition)
                        } else {
                            problems(response.code())
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        t.message?.let { makeToast(it) }
                    }
                })
            }
        }
        myRecyclerView.setHasFixedSize(true)

        findViewById<ProgressBar>(R.id.progressBar).visibility =
            ProgressBar.INVISIBLE
    }

    private fun problems(code: Int) {
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

    private fun makeToast(text: String) {
        Toast.makeText(
            this@MainActivity,
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("INIT", initPosts)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        callGet.cancel()
    }
}