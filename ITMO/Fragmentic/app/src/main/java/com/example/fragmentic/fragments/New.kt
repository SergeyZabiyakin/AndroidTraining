package com.example.fragmentic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fragmentic.R
import com.example.fragmentic.navigate

class New : Fragment() {
    private var int: Int = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new, container, false)
        view.findViewById<Button>(R.id.button).setOnClickListener {
            navigate(NewDirections.actionNew1ToNew1(++int))
            //findNavController().navigate(R.id.action_register_to_registered)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        int = NewArgs.fromBundle(requireArguments()).count
        view.findViewById<TextView>(R.id.button_text).text = int.toString()
    }
}
