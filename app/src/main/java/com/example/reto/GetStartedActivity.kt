package com.example.reto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.reto.ui.login.LoginActivity
import com.example.reto.ui.register.RegisterActivity

class GetStartedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        // Cek apakah ini pertama kali diluncurkan
        val sharedPref = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val isFirstLaunch = sharedPref.getBoolean("isFirstLaunch", true)

        // Jika aplikasi pertama kali diluncurkan, tampilkan Get Started
        if (isFirstLaunch) {
            setContentView(R.layout.activity_get_started)

            val loginButton: Button = findViewById(R.id.loginButton)
            val signupButton: Button = findViewById(R.id.signupButton)

            loginButton.setOnClickListener {
                // Pindah ke halaman LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

            signupButton.setOnClickListener {
                // Pindah ke halaman RegisterActivity
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            // Set flag isFirstLaunch ke false, agar halaman ini tidak muncul lagi
            sharedPref.edit().putBoolean("isFirstLaunch", false).apply()
        } else {
            // Jika bukan pertama kali diluncurkan, langsung ke MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Menutup GetStartedActivity
        }
    }
}
