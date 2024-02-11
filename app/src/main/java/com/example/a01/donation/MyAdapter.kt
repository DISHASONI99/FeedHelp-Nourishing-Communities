package com.example.a01.donation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a01.R
import com.google.firebase.firestore.auth.User

class MyAdapter(private val userList: ArrayList<Donation>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.explore_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val user: Donation = userList[position]
        holder.name.text = user.userId
        holder.cardboard.text = user.cardboardAmount.toString()
        holder.metal.text = user.metalAmount.toString()
        holder.plastic.text = user.plasticAmount.toString()
        holder.address.text = user.address
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.firstname)
        val cardboard : TextView = itemView.findViewById(R.id.cardboard)
        val metal : TextView = itemView.findViewById(R.id.metal)
        val plastic : TextView = itemView.findViewById(R.id.plastic)
        val address : TextView = itemView.findViewById(R.id.address)

    }
}