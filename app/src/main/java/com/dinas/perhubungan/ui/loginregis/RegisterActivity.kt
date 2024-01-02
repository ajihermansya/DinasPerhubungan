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

    private val daftarNIP = arrayOf(
        "196312131992021002",
        "197107041998031008",
        "198002222001121004",
        "196808181990031010",
        "197812282001121003",
        "198106022002121001",
        "198103142003121001",
        "197203111994032003",
        "196709031992021002",
        "198305142011012012",
        "198204042002121006",
        "196901262006041003",
        "198908172011011005",
        "196810012008011018",
        "198806252011011008",
        "197202062007012018",
        "196710282006041005",
        "198907122011011002",
        "196807042008011014",
        "197301162008011014",
        "197410182008011009",
        "197007272008011015",
        "197503012008012014",
        "196802062008011015",
        "197703042008011020",
        "197305172008011019",
        "197503282008011008",
        "197911112008011013",
        "196709302008011008",
        "197509232008011010",
        "196706222008011009",
        "197501172008011014",
        "196812252008011017",
        "197007042008011019",
        "197503132008011023",
        "197105272008011009",
        "197205032008011023",
        "196606292008011003",
        "197307012008011019",
        "1197908242008012030",
        "196604052008011011",
        "196605202008012010",
        "197611062009011005",
        "197412312009011058",
        "196812182008011006",
        "197502152008011024",
        "196707062008011021",
        "196801032008011012",
        "197805042008012034",
        "197508312008011007",
        "198503252010011027",
        "198208262010011021",
        "198205252010011032",
        "196903112008011019",
        "197406222007011007",
        "197008102007011031",
        "197307202009011008",
        "196808232009011004",
        "197105132009011006",
        "197706172008011018",
        "196608202007011013",
        "198206022009011006",
        "198305032009011013",
        "197306062007011019",
        "196610042009011002"

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.tvLogin.setOnClickListener {  startActivity(Intent(this, LoginActivity::class.java))  }

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
            val jabatan = binding.inputJabatan.text.toString()
            val tanggal = binding.tanggalKenaikan.text.toString()
            val tlpn = binding.inputTeleponwa.text.toString()
            val password = binding.inputPassword.text.toString()
            val confirmpassword = binding.inputConfirmPassword.text.toString()


            if (validateFields(nama_panjang, nip,  jabatan, tanggal,tlpn, password, confirmpassword)) {
                if (isRegisteredNIP(nip)) {
                    createUser(nama_panjang, nip, jabatan, tanggal, tlpn, password)
                } else {
                    showAlertDialog("NIP is not registered")
                }
            }
        }

    }


    private fun validateFields(
        namaPanjang: String,
        nip: String,
        jabatan: String,
        tanggal: String,
        tlpn: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (TextUtils.isEmpty(namaPanjang) || TextUtils.isEmpty(nip) || TextUtils.isEmpty(tlpn) || TextUtils.isEmpty(jabatan)
            || TextUtils.isEmpty(tanggal) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)
        ) {
            showAlertDialog("Please fill in all the fields")
            return false
        }

        val regexNama = Regex("^[a-zA-Z ]+\$")
        if (!regexNama.matches(namaPanjang)) {
            showAlertDialog("Full name should only contain letters and spaces")
            return false
        }

        if (!regexNama.matches(jabatan)) {
            showAlertDialog("Full name should only contain letters and spaces")
            return false
        }

        if (nip.length != 18 || !nip.matches(Regex("^[0-9]{18}$"))) {
            showAlertDialog("NIP must be exactly 18 digits and contain only numbers")
            return false
        }



        if (tlpn.length !in 11..13 || !Patterns.PHONE.matcher(tlpn).matches()) {
            showAlertDialog("Check your phone number")
            return false
        }

        val passwordPattern = Regex("^(?=.*[A-Z])(?=.*\\d).{6,}\$")
        if (password != confirmPassword || !password.matches(passwordPattern)) {
            showAlertDialog("Password must match Confirm Password and contain at least one uppercase letter and one digit")
            return false
        }

        return true
    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Validation Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImg = data.data!!
            binding.userImage.setImageURI(selectedImg)
        }
    }


    private fun isRegisteredNIP(nip: String): Boolean {
        return daftarNIP.contains(nip)
    }


    private fun createUser(
        namaPanjang: String,
        nip: String,
        jabatan: String,
        tanggal: String,
        tlpn: String,
        password: String
    ) {
        if (!::selectedImg.isInitialized) {
            showAlertDialog("Please select an image")
            return
        }
        showLoading(true)
        mAuth.createUserWithEmailAndPassword("$nip@dishub.com", password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    user?.let {
                        uploadImageToFirebase(nip, namaPanjang, jabatan, tanggal, tlpn, password)
                    }
                } else {
                    Toast.makeText(this, "Error creating user", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun uploadImageToFirebase(
        namaPanjang: String,
        nip: String,
        jabatan: String,
        tanggal: String,
        tlpn: String,
        password: String
    ) {
        val storageRef = storage.reference
        val imgRef = storageRef.child("images/${mAuth.currentUser?.uid}.jpg")

        imgRef.putFile(selectedImg)
            .addOnSuccessListener { taskSnapshot ->
                imgRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    addUserToDatabase(namaPanjang, nip, jabatan, tanggal, tlpn, password, imageUrl)
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
        nip: String,
        namaPanjang: String,
        jabatan: String,
        tanggal: String,
        tlpn: String,
        password: String,
        imageUrl: String
    ) {
        val email = "$nip@dishub.com"
        val database = FirebaseDatabase.getInstance()
        mDbRef = database.getReference()
        mDbRef.child("users").child(nip).setValue(UserModel(email, namaPanjang, jabatan, tanggal, tlpn, password, imageUrl))
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