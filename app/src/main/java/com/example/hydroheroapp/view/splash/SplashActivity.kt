package com.example.hydroheroapp.view.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hydroheroapp.R
import com.example.hydroheroapp.data.remote.repository.LoginPrefsRepo
import com.example.hydroheroapp.data.remote.repository.dataStore
import com.example.hydroheroapp.view.main.MainActivity
import com.example.hydroheroapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val content = findViewById<View>(android.R.id.content)
        @Suppress("UNUSED_EXPRESSION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            content.viewTreeObserver.addOnDrawListener { false }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val userLogin = runBlocking {
                LoginPrefsRepo.getInstance(this@SplashActivity.dataStore).getLoginStatus().first()
            }
            if (userLogin == true) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(applicationContext, WelcomeActivity::class.java))
                finish()
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, 1500L)
    }

}
