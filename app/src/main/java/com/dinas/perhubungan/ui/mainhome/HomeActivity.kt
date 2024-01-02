package com.dinas.perhubungan.ui.mainhome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dinas.perhubungan.R
import com.dinas.perhubungan.databinding.ActivityHomeBinding
import com.dinas.perhubungan.databinding.ActivityRegisterBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()



    }
}