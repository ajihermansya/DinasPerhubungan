package com.dinas.perhubungan.ui.mainhome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.dinas.perhubungan.data.PrefsManager
import com.dinas.perhubungan.databinding.ActivityHomeAdminBinding
import com.dinas.perhubungan.ui.loginregis.LoginActivity
import com.dinas.perhubungan.ui.menu_admin.DataPegawaiActivity
import com.dinas.perhubungan.ui.menu_admin.UploadDataActivity
import com.google.firebase.auth.FirebaseAuth


class HomeAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeAdminBinding
    private lateinit var prefsManager: PrefsManager
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        prefsManager = PrefsManager(this)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.dataPegawai.setOnClickListener {  startActivity(Intent(this, DataPegawaiActivity::class.java))  }
        binding.uploadData.setOnClickListener {  startActivity(Intent(this, UploadDataActivity::class.java))  }

        binding.aktivitasPengguna.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Under Development")
                .setMessage("This feature is currently under development. Stay tuned for updates!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
            val dialog = builder.create()
            dialog.show()
        }

        binding.tvKeluar.setOnClickListener {
            prefsManager.token = ""
            prefsManager.userEmail = ""
            prefsManager.isExampleLogin = false
            prefsManager.isAdminLoggedIn = false
            val intent = Intent(this@HomeAdminActivity, LoginActivity::class.java)
            startActivity(intent)
           finish()
        }

    }
}