package com.example.animation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter

class MainActivity : AppCompatActivity() {

    private var completeAnim = false
    private var controller = 0
    private lateinit var imageTop: ImageView
    private lateinit var imageBottom: ImageView
    private val images = arrayOf(
        R.drawable.item1,
        R.drawable.item2,
        R.drawable.item3,
        R.drawable.item4,
        R.drawable.item5
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val motionLayout = findViewById<MotionLayout>(R.id.motionLayout)
        imageBottom = findViewById(R.id.bottomImage)
        imageTop = findViewById(R.id.topImage)

        if (savedInstanceState != null) {
            controller = savedInstanceState.getInt("CONTROLLER")
            completeAnim = savedInstanceState.getBoolean("COMPLETEANIM")
        }

        imageTop.setImageResource(images[controller])
        imageBottom.setImageResource(images[(controller + 1) % 2])


        findViewById<ImageButton>(R.id.buttonLike).setOnClickListener {
            if (motionLayout.startState == R.id.startScene && motionLayout.progress == 0F) {
                motionLayout.transitionToState(R.id.like)
            }
        }

        findViewById<ImageButton>(R.id.buttonDislike).setOnClickListener {
            if (motionLayout.startState == R.id.startScene && motionLayout.progress == 0F) {
                motionLayout.transitionToState(R.id.dislike)
            }
        }

        motionLayout.setTransitionListener(object : TransitionAdapter() {

            //Я сделал ( -_-)(I_I)(-_- )

            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                when (currentId) {
                    R.id.offScreenDislike,
                    R.id.offScreenLike -> {
                        Log.e(
                            "MainActivity/TransitionCompleted",
                            "Progress : ${motionLayout.progress}"
                        )
                        if (motionLayout.progress == 1F) {
                            completeAnim = true
                            controller++
                            controller %= images.size
                            swapTopCard()
                            motionLayout.setTransition(R.id.startScene, R.id.like)
                            motionLayout.progress = 0F
                        }
                    }
                }
            }

            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                when (startId) {
                    R.id.startScene -> {
                        Log.e(
                            "MainActivity/TransitionStarted",
                            "Transition : startScene"
                        )
                        swapBottomCard()
                    }
                }
            }
        })
    }

    private fun swapTopCard() {
        imageTop.setImageResource(images[controller])
    }

    private fun swapBottomCard() {
        if (completeAnim) {
            completeAnim = false
            imageBottom.setImageResource(images[(controller + 1) % images.size])
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("CONTROLLER", controller)
        outState.putBoolean("COMPLETEANIM", completeAnim)
        super.onSaveInstanceState(outState)
    }
}