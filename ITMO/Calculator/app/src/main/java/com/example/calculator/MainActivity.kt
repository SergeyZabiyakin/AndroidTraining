package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.view.iterator
import java.lang.StringBuilder


class MainActivity : AppCompatActivity() {
    companion object {
        const val ACTION = "ACTION"
        const val NUM1 = "NUM1"
        const val NUM2 = "NUM2"
        const val SB = "SB"
        const val SBMini = "SBMini"
    }

    lateinit var Grid: GridLayout
    lateinit var TextViewMini: TextView
    lateinit var TextView: TextView
    lateinit var Button: Button
    var Sb = StringBuilder()
    var SbMini = StringBuilder()
    var action = ' '
    var num1 = 0L
    var num2 = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TextView = findViewById(R.id.TextView)
        TextViewMini = findViewById(R.id.TextViewMini)
        Grid = findViewById(R.id.Grid)
        Button = findViewById(R.id.calculation)

        val listenerAction = View.OnClickListener {
            if (action == ' ') {
                action = (it as Button).text.toString()[1]
                updateView(it)
            }
        }

        val listenerNoAction = View.OnClickListener {
            if (action == ' ') {
                num1 *= 10
                num1 += (it as Button).text.toString().toLong()
            } else {
                num2 *= 10
                num2 += (it as Button).text.toString().toLong()
            }
            updateView((it))
        }

        for (button in Grid) {
            if (button is Button) {
                if (button.text.length < 2) {
                    button.setOnClickListener(listenerNoAction)
                } else {
                    button.setOnClickListener(listenerAction)
                }
            }
        }

        Button.setOnClickListener {
            Sb.append('=')
            if (action == ' ') action = '+'
            when (action) {
                '√' -> {
                    Sb.append(Math.sqrt(num2.toDouble()))
                }
                '*' -> {
                    Sb.append(num1 * num2)
                }
                '-' -> {
                    Sb.append(num1 - num2)
                }
                '+' -> {
                    Sb.append(num1 + num2)
                }
                '/' -> {
                    Sb.append(num1.toDouble() / num2.toDouble())
                }
            }
            num1 = 0L
            num2 = 0L
            action = ' '
            SbMini.clear()
            SbMini.append(Sb)
            TextViewMini.setText(Sb.toString())
            TextView.setText("")
            Sb.clear()
        }
    }

    protected fun updateView(btn: Button) {
        Sb.append(btn.text)
        TextView.setText(Sb.toString())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SBMini, SbMini.toString())
        outState.putString(SB, Sb.toString())
        outState.putLong(NUM1,num1)
        outState.putLong(NUM2,num2)
        outState.putChar(ACTION,action)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        SbMini.append(savedInstanceState.getString(SBMini))
        Sb.append(savedInstanceState.getString(SB))
        num1=savedInstanceState.getLong(NUM1)
        num2=savedInstanceState.getLong(NUM2)
        action=savedInstanceState.getChar(ACTION)
        TextViewMini.setText(SbMini)
        TextView.setText(Sb)
    }
}