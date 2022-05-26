package com.example.securicamapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.securicamapplication.databinding.ActivitySplashBinding
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class Splash_Activity : AppCompatActivity() {
    private lateinit var splashBinding: ActivitySplashBinding
    private val handler = Handler(Looper.getMainLooper())

    private var valueDelay = 3500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)

        val actionbar = supportActionBar

        actionbar?.title = " "


        handler.postDelayed({
            val splashIntent = Intent(this, LogInActivity::class.java)
            startActivity(splashIntent)
            finish()
        }, valueDelay)
    }
}