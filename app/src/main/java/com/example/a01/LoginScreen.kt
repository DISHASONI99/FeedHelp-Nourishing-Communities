package com.example.a01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        val login = findViewById<Button>(R.id.login)
        login.setOnClickListener {
            startActivity(Intent(this@LoginScreen, MainActivity::class.java))
            finish()
        }

        val register = findViewById<TextView>(R.id.register)
        register.setOnClickListener {
            startActivity(Intent(this@LoginScreen, RegisterActivity::class.java))
            finish()
        }
        val forgotPassword = findViewById<TextView>(R.id.fpwd)
        forgotPassword.setOnClickListener {
            startActivity(Intent(this,ForgotPassword::class.java))
            finish()
        }

    }
}