package com.example.hydroheroapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.hydroheroapp.R
import com.example.hydroheroapp.data.Result
import com.example.hydroheroapp.data.remote.repository.LoginPrefsRepo
import com.example.hydroheroapp.data.remote.repository.dataStore
import com.example.hydroheroapp.databinding.ActivityLoginBinding
import com.example.hydroheroapp.view.ViewModelFactory
import com.example.hydroheroapp.view.main.MainActivity
import com.example.hydroheroapp.view.register.RegisterActivity

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

        playAnimation()

        binding.progressBar.visibility = View.GONE

        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(
                this,
                LoginPrefsRepo.getInstance(dataStore)
            )
        val viewModel: LoginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edLoginEmail.error = getString(R.string.input_email)
                }

                password.isEmpty() -> {
                    binding.edLoginPassword.error = getString(R.string.input_password)
                }

                else -> {
                    viewModel.login(email, password).observe(this) {
                        if (it != null) {
                            when (it) {
                                is Result.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }

                                is Result.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    val response = it.data
                                    viewModel.saveState(response.email.toString())
                                    AlertDialog.Builder(this).apply {
                                        setTitle(getString(R.string.success))
                                        setMessage(getString(R.string.welcome_back) + " " + "${response.username}")
                                        setPositiveButton(getString(R.string.continue_dialog)) { _, _ ->
                                            val intent =
                                                Intent(this@LoginActivity, MainActivity::class.java)
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                        }
                                        create()
                                        show()
                                    }.apply {
                                        setOnCancelListener {
                                            val intent =
                                                Intent(this@LoginActivity, MainActivity::class.java)
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                        }
                                        show()
                                    }
                                }

                                is Result.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    AlertDialog.Builder(this).apply {
                                        setTitle(getString(R.string.error))
                                        setMessage(it.error)
                                        setPositiveButton(getString(R.string.continue_dialog)) { _, _ -> }
                                        create()
                                        show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val welcomeText = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(100)
        val messageText = ObjectAnimator.ofFloat(binding.tvMessage, View.ALPHA, 1f).setDuration(100)
        val emailText = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(100)
        val emailInput = ObjectAnimator.ofFloat(binding.tilEmail, View.ALPHA, 1f).setDuration(100)
        val passwordText =
            ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(100)
        val passwordInput =
            ObjectAnimator.ofFloat(binding.tilPassword, View.ALPHA, 1f).setDuration(100)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(100)
        val dontHaveAccount =
            ObjectAnimator.ofFloat(binding.tvDontHaveAnAccount, View.ALPHA, 1f).setDuration(100)
        val registerText =
            ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(dontHaveAccount, registerText)
        }

        AnimatorSet().apply {
            playSequentially(
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