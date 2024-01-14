package com.example.a01

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        auth = FirebaseAuth.getInstance()
        val forgotPassword = findViewById<Button>(R.id.forgot_password)
        val email = findViewById<EditText>(R.id.email)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)


        forgotPassword.setOnClickListener {
            val email = findViewById<EditText>(R.id.email)
            if(email.text.toString().isEmpty()) {
                email.error = "Please enter your email"
                email.requestFocus()
                return@setOnClickListener
            }
            else if(!email.text.toString().contains("@")) {
                email.error = "Please enter a valid email"
                email.requestFocus()
                return@setOnClickListener
            }
            else {
                forgetPass()
            }
        }
    }

    private fun forgetPass() {
        val email = findViewById<EditText>(R.id.email).text.toString()
        progressDialog.setMessage("Please Wait...")
        progressDialog.show()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                progressDialog.dismiss()
                Toast.makeText(this@ForgotPassword, "Reset link sent to gmail", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, LoginScreen::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this@ForgotPassword, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}