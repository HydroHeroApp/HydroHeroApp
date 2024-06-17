package com.example.hydroheroapp.view.register

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
import com.example.hydroheroapp.data.remote.repository.LoginPrefsRepo
import com.example.hydroheroapp.data.remote.repository.dataStore
import com.example.hydroheroapp.databinding.ActivityRegisterBinding
import com.example.hydroheroapp.view.ViewModelFactory
import com.example.hydroheroapp.view.login.LoginActivity
import com.example.hydroheroapp.data.Result
import com.example.hydroheroapp.view.welcome.WelcomeActivity

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

        playAnimation()

        binding.progressBar.visibility = View.GONE

        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(
                this,
                LoginPrefsRepo.getInstance(dataStore)
            )
        val viewModel: RegisterViewModel =
            ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val username = binding.edRegisterUsername.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            viewModel.register(username, email, password).observe(this) {
                if (it != null) {
                    when (it) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val response = it.data
                            AlertDialog.Builder(this).apply {
                                setTitle(getString(R.string.success))
                                setMessage(response.message)
                                setPositiveButton(getString(R.string.continue_dialog)) { _, _ ->
                                    startActivity(
                                        Intent(
                                            this@RegisterActivity,
                                            WelcomeActivity::class.java
                                        )
                                    )
                                }
                                create()
                                show()
                            }.apply {
                                setOnCancelListener {
                                    startActivity(
                                        Intent(
                                            this@RegisterActivity,
                                            WelcomeActivity::class.java
                                        )
                                    )
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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

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
        val btnRegister =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(100)
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