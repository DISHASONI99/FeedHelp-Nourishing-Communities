package com.example.a01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val login = findViewById<TextView>(R.id.login)
        login.setOnClickListener{
            startActivity(Intent(this,LoginScreen::class.java))
        }
    }
}