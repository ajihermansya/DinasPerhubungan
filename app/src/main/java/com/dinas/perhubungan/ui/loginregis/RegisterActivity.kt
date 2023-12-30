package com.dinas.perhubungan.ui.loginregis

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.dinas.perhubungan.data.model.UserModel
import com.dinas.perhubungan.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var mDbRef: DatabaseReference
    private lateinit var selectedImg: Uri
    private lateinit var dialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        dialog = AlertDialog.Builder(this)
            .setMessage("Updating Profile...")
            .setCancelable(false)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        mAuth = FirebaseAuth.getInstance()

        binding.loginshowPwd.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Show Password
                binding.inputPassword.transformationMethod = null
                binding.inputConfirmPassword.transformationMethod = null
            } else {
                // Hide Password
                binding.inputPassword.transformationMethod = PasswordTransformationMethod()
                binding.inputConfirmPassword.transformationMethod = PasswordTransformationMethod()
            }
        }

        binding.userImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        binding.daftar.setOnClickListener {
           val nama_panjang = binding.namaLengkap.text.toString()
            val nip = binding.inputNip.text.toString()
            val tlpn = binding.inputTeleponwa.text.toString()
            val password = binding.inputPassword.text.toString()
            val confirmpassword = binding.inputConfirmPassword.text.toString()

            signUp(nama_panjang, nip, tlpn, password, confirmpassword)
        }

    }


    private fun signUp(
        nama_panjang: String,
        nip: String,
        tlpn: String,
        confirmpassword : String,
        password: String
    ) {
        // Validation for empty fields
        if (TextUtils.isEmpty(nama_panjang) || TextUtils.isEmpty(nip) || TextUtils.isEmpty(tlpn) || TextUtils.isEmpty(confirmpassword) || TextUtils.isEmpty(password)) {
            Toast.makeText(this@RegisterActivity, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }


        val regex = Regex("^[a-zA-Z ]+\$")
        if (!regex.matches(nama_panjang)) {
            Toast.makeText(this@RegisterActivity, "Nama Panjang harus berisi huruf dan spasi saja", Toast.LENGTH_SHORT).show()
            return
        }

        if (nip.length != 18 || !nip.matches(Regex("\\d+"))) {
            Toast.makeText(this@RegisterActivity, "NIP harus memiliki panjang 18 digit dan hanya berisi angka", Toast.LENGTH_SHORT).show()
            return
        }



        if (tlpn.length <= 10 || tlpn.length > 13) {
            Toast.makeText(this@RegisterActivity, "Periksa nomor telepon anda", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(confirmpassword) || TextUtils.isEmpty(password)) {
            Toast.makeText(this@RegisterActivity, "Password dan Konfirmasi Password harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmpassword) {
            Toast.makeText(this@RegisterActivity, "Password dan Konfirmasi Password harus sama", Toast.LENGTH_SHORT).show()
            return
        }



        // Proses Sign-Up
        showLoading(true)
        mAuth.createUserWithEmailAndPassword("$nip@example.com", password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    //code for jumping to home
                    addUserToDatabase(nama_panjang, nip, tlpn, password, mAuth.currentUser?.uid!!)
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "Error creating user", Toast.LENGTH_SHORT).show()
                }
            }


    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun addUserToDatabase(nama_panjang: String, nip: String, tlpn: String, password: String, confirmpassword: String) {
        val email = "$nip@example.com"
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("admin").child(nip).setValue(UserModel(nama_panjang, email, tlpn, password, confirmpassword))
    }



}