package com.example.a01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.hide()
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val name: TextView = findViewById(R.id.textView2)
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val uid = currentUser?.uid
        val back = findViewById<ImageButton>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this@Profile, MainActivity::class.java)
            startActivity(intent)
        }

        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid.toString())
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userName = snapshot.child("name").value.toString()
                    name.text = userName
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Profile, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}