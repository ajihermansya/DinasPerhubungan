package com.dinas.perhubungan.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dinas.perhubungan.R
import com.dinas.perhubungan.data.PrefsManager
import com.dinas.perhubungan.databinding.ActivityBacklogBinding
import com.dinas.perhubungan.ui.loginregis.LoginActivity

class BacklogActivity : AppCompatActivity() {
    private lateinit var prefsManager: PrefsManager
    private lateinit var binding : ActivityBacklogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBacklogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        prefsManager = PrefsManager(this)
        val btnStart = findViewById<Button>(R.id.btn_start)
        btnStart.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            prefsManager.isExampleLogin = true
            finish()
        }
    }
}