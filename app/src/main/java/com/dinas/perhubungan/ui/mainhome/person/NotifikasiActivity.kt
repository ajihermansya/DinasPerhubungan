package com.dinas.perhubungan.ui.mainhome.person

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dinas.perhubungan.R
import com.dinas.perhubungan.databinding.ActivityNotifikasiBinding
import com.dinas.perhubungan.databinding.ActivityRegisterBinding

class NotifikasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotifikasiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.iconBack.setOnClickListener {
            finish()
        }
    }
}