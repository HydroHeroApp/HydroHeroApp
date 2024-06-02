package com.example.hydroheroapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hydroheroapp.R
import com.example.hydroheroapp.databinding.ActivityLoginBinding
import com.example.hydroheroapp.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        playAnimation()
    }

    private fun playAnimation() {
        val image = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(100)
        val welcomeText = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(100)
        val messageText = ObjectAnimator.ofFloat(binding.tvMessage, View.ALPHA, 1f).setDuration(100)
        val emailText = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(100)
        val emailInput = ObjectAnimator.ofFloat(binding.tilEmail, View.ALPHA, 1f).setDuration(100)
        val passwordText =
            ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(100)
        val passwordInput =
            ObjectAnimator.ofFloat(binding.tilPassword, View.ALPHA, 1f).setDuration(100)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(100)
        val dontHaveAccount = ObjectAnimator.ofFloat(binding.tvDontHaveAnAccount, View.ALPHA, 1f)
            .setDuration(100)
        val registerText =
            ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(
                dontHaveAccount,
                registerText
            )
        }

        AnimatorSet().apply {
            playSequentially(
                image,
                title,
                welcomeText,
                messageText,
                emailText,
                emailInput,
                passwordText,
                passwordInput,
                btnLogin,
                together
                )
            startDelay = 100
        }.start()
    }
}