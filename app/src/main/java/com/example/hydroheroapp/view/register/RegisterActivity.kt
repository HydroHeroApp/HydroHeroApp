package com.example.hydroheroapp.view.register

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
import com.example.hydroheroapp.databinding.ActivityRegisterBinding
import com.example.hydroheroapp.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        playAnimation()
    }

    private fun playAnimation() {
        val image = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(100)
        val joinText = ObjectAnimator.ofFloat(binding.tvJoin, View.ALPHA, 1f).setDuration(100)
        val messageText = ObjectAnimator.ofFloat(binding.tvMessage, View.ALPHA, 1f).setDuration(100)
        val nameText = ObjectAnimator.ofFloat(binding.tvUsername, View.ALPHA, 1f).setDuration(100)
        val nameInput = ObjectAnimator.ofFloat(binding.tilUsername, View.ALPHA, 1f).setDuration(100)
        val emailText = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(100)
        val emailInput = ObjectAnimator.ofFloat(binding.tilEmail, View.ALPHA, 1f).setDuration(100)
        val passwordText =
            ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(100)
        val passwordInput =
            ObjectAnimator.ofFloat(binding.tilPassword, View.ALPHA, 1f).setDuration(100)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(100)
        val haveAccountText =
            ObjectAnimator.ofFloat(binding.tvAlreadyHaveAnAccount, View.ALPHA, 1f).setDuration(100)
        val loginText = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(
                haveAccountText,
                loginText
            )
        }

        AnimatorSet().apply {
            playSequentially(
                image,
                joinText,
                messageText,
                nameText,
                nameInput,
                emailText,
                emailInput,
                passwordText,
                passwordInput,
                btnRegister,
                together
            )
            startDelay = 100
        }.start()

    }
}