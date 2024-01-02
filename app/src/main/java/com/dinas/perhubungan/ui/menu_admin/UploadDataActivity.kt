package com.dinas.perhubungan.ui.menu_admin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.dinas.perhubungan.R
import com.dinas.perhubungan.databinding.ActivityHomeAdminBinding
import com.dinas.perhubungan.databinding.ActivityUploadDataBinding

class UploadDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadDataBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        val webView = findViewById<WebView>(R.id.webView)

        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                view.loadUrl("javascript:alert('Data Berhasil Dimuat')")
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView, url: String, message: String, result: android.webkit.JsResult): Boolean {
                Toast.makeText(this@UploadDataActivity, message, Toast.LENGTH_LONG).show()
                result.confirm()
                return true
            }
        }

        webView.loadUrl("https://docs.google.com/spreadsheets/d/e/2PACX-1vTyws9H_AkENWk5as0EZyETX1wAskKp39E__Eo219DR4eAbW5HRn9LDVyLkfnv8PVm0F7lXyrnEzdOx/pubhtml")


        binding.editData.setOnClickListener {
            val url = "https://docs.google.com/spreadsheets/d/1upeIRUT1x-fPdBdq6X7sM8aSe7QHbCACFvRfROhCnpc/edit?usp=sharing"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener { finish() }

    }
}