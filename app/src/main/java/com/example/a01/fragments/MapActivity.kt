package com.example.a01.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.a01.DonateList
import com.example.a01.MapMarker
import com.example.a01.R

class MapActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map_activity, container, false)

        val button: Button = view.findViewById(R.id.button)
        button.setOnClickListener {
            val intent = Intent(requireContext(), MapMarker::class.java)
            startActivity(intent)
        }

        return view
    }
}