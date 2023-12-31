package com.dinas.perhubungan.ui.loginregis

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.dinas.perhubungan.databinding.ActivityLoginBinding
import com.dinas.perhubungan.ui.mainhome.HomeActivity
import com.dinas.perhubungan.ui.mainhome.HomeAdminActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        Animation()

        binding.tvDaftar.setOnClickListener {  startActivity(Intent(this, RegisterActivity::class.java))  }

        mAuth = FirebaseAuth.getInstance()

        binding.loginshowPwd.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Show Password
                binding.passwordNp.transformationMethod = null
            } else {
                // Hide Password
                binding.passwordNp.transformationMethod = PasswordTransformationMethod()
            }
        }

        binding.loginUser.setOnClickListener {
            val nip = binding.usernameNip.text.toString()
            val password = binding.passwordNp.text.toString()

            loginUp(nip, password)
            loginAdmin(nip, password)
        }

    }

    private fun loginUp(nip: String, password: String) {
        if (TextUtils.isEmpty(nip) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading(true)

        mAuth.signInWithEmailAndPassword("$nip@dishub.com", password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    if (user != null) {
                        val email = user.email ?: "$nip@dishub.com"
                        if (email.endsWith("@dishub.com")) {
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            loginAdmin(nip, password)
                        }
                    }
                } else {
                    showLoginFailedDialog()
                }
            }
    }

    private fun loginAdmin(nip: String, password: String) {
        mAuth.signInWithEmailAndPassword("$nip@gmail.com", password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    if (user != null) {
                        val email = user.email ?: "$nip@gmail.com"
                        if (email.endsWith("@gmail.com")) {
                            val intent = Intent(this, HomeAdminActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
    }

    private fun Animation() {
        ObjectAnimator.ofFloat(binding.imageView3, View.TRANSLATION_Y, -20f, 20f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoginFailedDialog() {
        val builder = AlertDialog.Builder(this@LoginActivity)
        builder.setTitle("Login Failed")
            .setMessage("Invalid NIP or password. Please try again.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

}