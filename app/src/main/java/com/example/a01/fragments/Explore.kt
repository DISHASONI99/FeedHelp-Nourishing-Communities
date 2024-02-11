package com.example.a01.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a01.R
import com.example.a01.donation.Donation
import com.example.a01.donation.MyAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject


class Explore : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<Donation>
    private lateinit var myAdapter: MyAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()

        myAdapter = MyAdapter(userArrayList)

        recyclerView.adapter = myAdapter

        EventChangeListener()

        return view
    }

//    private fun EventChangeListener(){
//        db = FirebaseFirestore.getInstance()
//        db.collection("Food_orders").addSnapshotListener(
//            object: EventListener<QuerySnapshot>{
//                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
//                    if(error!=null){
//                        Log.e("Firestore Error", error.message.toString())
//                        return
//                    }
//
//                    for(dc: DocumentChange in value?.documentChanges!!){
//
//                        if(dc.type==DocumentChange.Type.ADDED){
//                            userArrayList.add(dc.document.toObject(Donation:class.java))
//                        }
//                    }
//                    myAdapter.notifyDataSetChanged()
//                }
//            } )
//    }
    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("Food_orders").addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore Error", error.message.toString())
                return@addSnapshotListener
            }

            for (dc: DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    userArrayList.add(dc.document.toObject(Donation::class.java))
                }
            }
            myAdapter.notifyDataSetChanged()
        }
    }
}