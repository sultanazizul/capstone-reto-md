package com.example.reto.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.reto.databinding.ActivityRegisterBinding
import com.example.reto.ui.ViewModelFactory
import com.example.reto.ui.login.LoginActivity

class RegisterActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel.isRegistered.observe(this) {
            if (it) {
                Toast.makeText(this, "Berhasil Mendaftar", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        binding.haveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerButton.setOnClickListener {
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (email.isEmpty()) {
                binding.emailEditTextLayout.error = "Email tidak boleh kosong"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEditTextLayout.error = "Email tidak valid"
            } else if (password.isEmpty()) {
                binding.passwordEditTextLayout.error = "Password tidak boleh kosong"
            } else if (password.length < 8) {
                binding.passwordEditTextLayout.error = "Password harus terdiri dari minimal 6 karakter"
            } else {
                // Clear error messages before proceeding with login
                binding.emailEditTextLayout.error = null
                binding.passwordEditTextLayout.error = null

                try {
                    viewModel.register(email, password)
                } catch (e: Exception) {
                    Log.e("RegisterActivity", "Error occurred during register: ${e.message}")
                    Toast.makeText(this, "Terjadi kesalahan saat mendaftar. Silakan coba lagi.", Toast.LENGTH_LONG).show()
                } finally {
                    binding.progressBar.visibility = View.GONE
                    binding.registerButton.visibility = View.VISIBLE
                }
            }
        }
    }
}