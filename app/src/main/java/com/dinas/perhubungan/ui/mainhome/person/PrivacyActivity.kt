package com.dinas.perhubungan.ui.mainhome.person

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.dinas.perhubungan.R
import com.dinas.perhubungan.databinding.ActivityNotifikasiBinding
import com.dinas.perhubungan.databinding.ActivityPrivacyBinding

class PrivacyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.iconBack.setOnClickListener {
            finish()
        }

        val email = binding.root.findViewById<TextView>(R.id.to_email)

        email.setOnClickListener {
            val recipientEmail = getString(R.string.emails)
            val subject = getString(R.string.subject_email)
            val message = getString(R.string.isi_pesan)

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$recipientEmail")
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
            }

            if (intent.resolveActivity(this@PrivacyActivity.packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this@PrivacyActivity, R.string.empty_apps, Toast.LENGTH_SHORT).show()
            }
        }

    }
}