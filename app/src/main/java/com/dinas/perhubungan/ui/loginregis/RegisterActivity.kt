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


            if (validateFields(nama_panjang, nip, tlpn, password, confirmpassword)) {
                createUser(nama_panjang, nip, tlpn, password)
            }
        }

    }


    private fun validateFields(
        namaPanjang: String,
        nip: String,
        tlpn: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (TextUtils.isEmpty(namaPanjang) || TextUtils.isEmpty(nip) || TextUtils.isEmpty(tlpn)
            || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)
        ) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return false
        }

        val regexNama = Regex("^[a-zA-Z ]+\$")
        if (!regexNama.matches(namaPanjang)) {
            Toast.makeText(this, "Full name should only contain letters and spaces", Toast.LENGTH_SHORT).show()
            return false
        }

        if (nip.length != 18 || !nip.matches(Regex("^[0-9]{18}$"))) {
            Toast.makeText(this, "NIP must be exactly 18 digits and contain only numbers", Toast.LENGTH_SHORT).show()
            return false
        }

        if (tlpn.length !in 11..13 || !Patterns.PHONE.matcher(tlpn).matches()) {
            Toast.makeText(this, "Check your phone number", Toast.LENGTH_SHORT).show()
            return false
        }

        val passwordPattern = Regex("^(?=.*[A-Z])(?=.*\\d).{6,}\$")
        if (password != confirmPassword || !password.matches(passwordPattern)) {
            Toast.makeText(this, "Password must match Confirm Password and contain at least one uppercase letter and one digit", Toast.LENGTH_SHORT).show()
            return false
        }


        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImg = data.data!!
            binding.userImage.setImageURI(selectedImg)
        }
    }

    private fun createUser(
        namaPanjang: String,
        nip: String,
        tlpn: String,
        password: String
    ) {
        if (!::selectedImg.isInitialized) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }
        showLoading(true)
        mAuth.createUserWithEmailAndPassword("$nip@dishub.com", password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    user?.let {
                        uploadImageToFirebase(namaPanjang, nip, tlpn, password)
                    }
                } else {
                    Toast.makeText(this, "Error creating user", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun uploadImageToFirebase(
        namaPanjang: String,
        nip: String,
        tlpn: String,
        password: String
    ) {
        val storageRef = storage.reference
        val imgRef = storageRef.child("images/${mAuth.currentUser?.uid}.jpg")

        imgRef.putFile(selectedImg)
            .addOnSuccessListener { taskSnapshot ->
                imgRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    addUserToDatabase(namaPanjang, nip, tlpn, password, imageUrl)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        showLoading(false)
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun addUserToDatabase(
        namaPanjang: String,
        nip: String,
        tlpn: String,
        password: String,
        imageUrl: String
    ) {
        val email = "$nip@dishub.com"
        val database = FirebaseDatabase.getInstance()
        mDbRef = database.getReference()
        mDbRef.child("users").child(nip).setValue(UserModel(namaPanjang, email, tlpn, password, imageUrl))
            .addOnSuccessListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}