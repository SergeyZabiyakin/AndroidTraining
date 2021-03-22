package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val THEME = "THEME"
        const val THEME_LIGHT = 0
        const val THEME_DARK = 1
    }

    var themeMode = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        themeDark.setOnClickListener {
            setTHeme(AppCompatDelegate.MODE_NIGHT_YES)
            themeMode = 1
            themeLight.isChecked = false
        }
        themeLight.setOnClickListener {
            setTHeme(AppCompatDelegate.MODE_NIGHT_NO)
            themeMode = 0
            themeDark.isChecked = false
        }
        initTheme(themeMode)
    }

    private fun setTHeme(themeMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    private fun initTheme(themeMode: Int) {
        when (themeMode) {
            THEME_LIGHT -> {
                themeLight.isChecked = true
            }
            THEME_DARK -> {
                themeDark.isChecked = true
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(THEME, themeMode)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        themeMode=savedInstanceState.getInt(THEME)

    }
}