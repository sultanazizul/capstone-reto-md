package com.example.reto.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.reto.R
import com.example.reto.databinding.ActivityLoginBinding
import com.example.reto.ui.ViewModelFactory
import com.example.reto.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        loginViewModel.isLoading.observe(this) {
            showLoading(it)
            Toast.makeText(this, "Berhasil Masuk", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupAction() {
        binding.noAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (email.isEmpty()) {
                binding.emailEditTextLayout.error = resources.getString(R.string.cannot_empty)
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEditTextLayout.error = resources.getString(R.string.email_invalid)
            } else if (password.isEmpty()) {
                binding.passwordEditTextLayout.error = resources.getString(R.string.cannot_empty)
            } else if (password.length < 8) {
                binding.passwordEditTextLayout.error = "Password harus terdiri dari minimal 8 karakter"
            } else {
                // Clear error messages before proceeding with login
                binding.emailEditTextLayout.error = null
                binding.passwordEditTextLayout.error = null

                try {
                    loginViewModel.login(email, password)
                } catch (e: Exception) {
                    Log.e("LoginActivity", "Error occurred during login: ${e.message}")
                    showAlertDialog(
                        "Login Gagal",
                        "Terjadi kesalahan saat login. Silakan coba lagi."
                    )
                } finally {
                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showAlertDialog(title: String, message: String, onPositiveClick: (() -> Unit)? = null) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            onPositiveClick?.invoke()
            dialog.dismiss()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(1000)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(1000)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(1000)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(1000)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(1000)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(1000)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(1000)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }
}
