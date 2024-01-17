package com.example.a01.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide.init
import com.example.a01.DonateList
import com.example.a01.ImageAdapter
import com.example.a01.MainActivity
import com.example.a01.R


class Donate : Fragment() {

    private lateinit var  viewPager2: ViewPager2
    private lateinit var handler : Handler
    private lateinit var imageList:ArrayList<Int>
    private lateinit var adapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_donate, container, false)

        val button: Button = view.findViewById(R.id.button)
        button.setOnClickListener {
            val intent = Intent(requireContext(), DonateList::class.java)
            startActivity(intent)
        }

        return view
    }
}