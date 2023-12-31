package com.dinas.perhubungan.ui.loginregis

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dinas.perhubungan.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.tvDaftar.setOnClickListener {  startActivity(Intent(this, RegisterActivity::class.java))  }
        Animation()
    }

    private fun Animation() {
        ObjectAnimator.ofFloat(binding.imageView3, View.TRANSLATION_Y, -20f, 20f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

    }


}