package com.dinas.perhubungan.ui.menu_admin.detail_jabatan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dinas.perhubungan.R
import com.dinas.perhubungan.databinding.ActivityDetailJabatanBinding
import com.dinas.perhubungan.databinding.ActivityNotifikasiBinding

class DetailJabatanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailJabatanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJabatanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.iconBack.setOnClickListener {
            finish()
        }
    }
}