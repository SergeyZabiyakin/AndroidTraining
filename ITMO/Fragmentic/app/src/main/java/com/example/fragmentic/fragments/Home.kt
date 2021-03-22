package com.example.fragmentic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.fragmentic.R
import com.example.fragmentic.navigate

/**
 * Shows the main title screen with a button that navigates to [About].
 */
class Home : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        //view.findViewById<TextView>(R.id.button_text).text = int.toString()
        view.findViewById<Button>(R.id.button).setOnClickListener {
            navigate(HomeDirections.actionHomeToNew1(1))
        }
        return view
    }
}
