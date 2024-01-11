package com.example.a01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val executor = Executors.newSingleThreadScheduledExecutor()
        executor.schedule({
            Handler(Looper.getMainLooper()).post {
                val intent = Intent(this@SplashScreen, LoginScreen::class.java)
                startActivity(intent)
                finish()
            }
        }, 3, TimeUnit.SECONDS)
    }
}