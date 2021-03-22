package com.example.fragmentic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.fragmentic.R
import com.example.fragmentic.navigate

class Dictionary : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        //view.findViewById<TextView>(R.id.button_text).text = int.toString()
        view.findViewById<Button>(R.id.button).setOnClickListener {
            navigate(DictionaryDirections.actionDictionaryToNew1(1))
        }
        return view
    }

}
