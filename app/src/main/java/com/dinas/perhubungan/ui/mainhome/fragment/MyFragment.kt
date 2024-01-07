package com.dinas.perhubungan.ui.mainhome.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.dinas.perhubungan.R
import com.dinas.perhubungan.data.PrefsManager
import com.dinas.perhubungan.data.model.UserModel
import com.dinas.perhubungan.databinding.FragmentMyBinding
import com.dinas.perhubungan.ui.loginregis.LoginActivity
import com.dinas.perhubungan.ui.mainhome.person.AkunActivity
import com.dinas.perhubungan.ui.mainhome.person.EditProfileActivity
import com.dinas.perhubungan.ui.mainhome.person.NotifikasiActivity
import com.dinas.perhubungan.ui.mainhome.person.PrivacyActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore


class MyFragment : Fragment() {
    private lateinit var binding: FragmentMyBinding
    private lateinit var prefsManager: PrefsManager
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var userjabatanTextView: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyBinding.inflate(inflater, container, false)
        prefsManager = PrefsManager(requireContext())
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val language = binding.root.findViewById<LinearLayout>(R.id.language)
        val notif = binding.root.findViewById<LinearLayout>(R.id.notifikasi)
        val privasi = binding.root.findViewById<LinearLayout>(R.id.ketentuan_privasi)
        val akun = binding.root.findViewById<ImageView>(R.id.btn_akunsaya)
        val edit = binding.root.findViewById<ImageView>(R.id.btn_edit)
        userNameTextView = binding.root.findViewById(R.id.userNames)
        userEmailTextView = binding.root.findViewById(R.id.text_email)
        userjabatanTextView = binding.root.findViewById(R.id.jabatan_person)
        val keluarButton = binding.root.findViewById<Button>(R.id.riwayatKonseling)

        language.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        notif.setOnClickListener {
            val intent = Intent(requireContext(), NotifikasiActivity::class.java)
            startActivity(intent)
        }

        privasi.setOnClickListener {
            val intent = Intent(requireContext(), PrivacyActivity::class.java)
            startActivity(intent)
        }

        akun.setOnClickListener {
            showUnderDevelopmentDialog(requireContext())
        }

        edit.setOnClickListener {
            showUnderDevelopmentDialog(requireContext())
        }


            keluarButton.setOnClickListener {
            prefsManager.token = ""
            prefsManager.userEmail = ""
            prefsManager.isExampleLogin = false
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return binding.root
    }

    fun showUnderDevelopmentDialog(context: Context) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Aplikasi dalam Pengembangan")
            .setMessage("Maaf, fitur ini sedang dalam pengembangan.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Check apakah pengguna sudah masuk atau belum
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            fetchUserName(currentUser.uid)
        } else {
            Toast.makeText(requireContext(), "Anda belum masuk. Harap login terlebih dahulu.", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchUserName(nip: String) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(nip)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userModel = dataSnapshot.getValue(UserModel::class.java)
                    val userName = userModel?.nama_panjang ?: "Nama tidak tersedia"
                    val userNip = userModel?.nip?.replace(Regex("[^0-9]"), "") ?: "Nip tidak tersedia"
                    val userImage = userModel?.imageUrl ?: "Image tidak tersedia"
                    val userJabatan = userModel?.jabatan ?: "Jabatan tidak tersedia"

                    // Mengisi TextView dan ImageView dengan informasi pengguna dari Firebase
                    userNameTextView.text = userName
                    userEmailTextView.text = userNip
                    userjabatanTextView.text = userJabatan
                    val imageView = requireView().findViewById<ImageView>(R.id.userImage)

                    Glide.with(requireContext()).load(userImage).into(imageView)

                    // Menyimpan informasi pengguna ke SharedPreferences
                    saveUserToSharedPreferences(userName, userNip,userJabatan, userImage)
                } else {
                    Log.d("fetchUserName", "Dokumen pengguna tidak ditemukan")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("fetchUserName", "Gagal mendapatkan data pengguna", databaseError.toException())
            }
        })
    }

    private fun saveUserToSharedPreferences(userName: String, userNip: String, userJabatan: String,  userImage: String) {
        val sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("userName", userName)
        editor.putString("userNip", userNip)
        editor.putString("userJabatan", userJabatan)
        editor.putString("userImage", userImage)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        // Menampilkan informasi pengguna dari SharedPreferences saat fragment dibuka
        val sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", "") ?: ""
        val userNip = sharedPreferences.getString("userNip", "") ?: ""
        val userJabatan = sharedPreferences.getString("userJabatan", "") ?: ""
        val userImage = sharedPreferences.getString("userImage", "") ?: ""

        // Menampilkan informasi pengguna ke UI
        userNameTextView.text = userName
        userEmailTextView.text = userNip
        userjabatanTextView.text = userJabatan
        val imageView = requireView().findViewById<ImageView>(R.id.userImage)
        Glide.with(requireContext()).load(userImage).into(imageView)
    }
}