package com.dinas.perhubungan.ui.menu_admin.detail_jabatan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dinas.perhubungan.data.model.JabatanModel
import com.dinas.perhubungan.databinding.ActivityDetailJabatanBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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


        val nama = intent.getStringExtra("nama")
        val pangkat = intent.getStringExtra("pangkat")
        val golongan = intent.getStringExtra("golongan")
        val jabatan = intent.getStringExtra("jabatan")
        val tanggal = intent.getStringExtra("tanggal_kenaikan")


        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate = dateFormat.parse(tanggal ?: "")
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormatter.format(parsedDate ?: Date())

        binding.namaBiodata.text = nama
        binding.pangkatBiodata.text = pangkat
        binding.golonganBiodata.text = golongan
        binding.jabatanBiodata.text = jabatan
        binding.tanggalBiodata.text = formattedDate
    }
}
