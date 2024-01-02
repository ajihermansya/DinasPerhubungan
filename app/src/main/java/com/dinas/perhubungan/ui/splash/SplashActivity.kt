package com.dinas.perhubungan.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.dinas.perhubungan.R
import com.dinas.perhubungan.data.PrefsManager
import com.dinas.perhubungan.databinding.ActivitySplashBinding
import com.dinas.perhubungan.ui.loginregis.LoginActivity
import com.dinas.perhubungan.ui.mainhome.HomeActivity
import com.dinas.perhubungan.ui.mainhome.HomeAdminActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean("isFirstRun", true)

        lifecycleScope.launch {
            delay(2000)

            val intent = if (isFirstRun) {
                Intent(this@SplashActivity, BacklogActivity::class.java)
            } else {
                val prefsManager = PrefsManager(this@SplashActivity)
                if (prefsManager.isExampleLogin) {
                    Intent(this@SplashActivity, HomeActivity::class.java)
                }
                if (prefsManager.isAdminLoggedIn) {
                    Intent(this@SplashActivity, HomeAdminActivity::class.java)
                }
                else {
                    Intent(this@SplashActivity, LoginActivity::class.java)
                }
            }

            startActivity(intent)
            finish()
        }

        // Tandai aplikasi sudah dijalankan jika ini adalah pertama kalinya
        if (isFirstRun) {
            prefs.edit().putBoolean("isFirstRun", false).apply()
        }
    }
}