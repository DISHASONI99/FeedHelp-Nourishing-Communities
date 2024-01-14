package com.example.a01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class SplashScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        auth = FirebaseAuth.getInstance()

        val executor = Executors.newSingleThreadScheduledExecutor()
        executor.schedule({
            Handler(Looper.getMainLooper()).post {
                val firebaseUser = auth.currentUser
                if (firebaseUser != null) {
                    val intent = Intent(this@SplashScreen, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    val intent = Intent(this@SplashScreen, OnboardingActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 3, TimeUnit.SECONDS)
    }
}