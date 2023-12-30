package com.dinas.perhubungan.ui.loginregis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dinas.perhubungan.R
import com.dinas.perhubungan.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}