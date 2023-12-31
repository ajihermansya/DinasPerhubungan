package com.dinas.perhubungan.ui.mainhome.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.dinas.perhubungan.R
import com.dinas.perhubungan.data.model.UserModel
import com.dinas.perhubungan.databinding.FragmentNotifikasiBinding
import com.dinas.perhubungan.ui.loginregis.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class NotifikasiFragment : Fragment() {
    private lateinit var binding: FragmentNotifikasiBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userTanggalTextView: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotifikasiBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val notif = binding.root.findViewById<Switch>(R.id.notif_switch)
        userTanggalTextView = binding.root.findViewById(R.id.tanggal_notif)

        return binding.root
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
                    val tanggal = userModel?.tanggal ?: "Tanggal tidak tersedia"


                    // Mengisi TextView dan ImageView dengan informasi pengguna dari Firebase
                    userTanggalTextView.text = tanggal


                    saveUserToSharedPreferences(tanggal)
                } else {
                    Log.d("fetchUserName", "Dokumen pengguna tidak ditemukan")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("fetchUserName", "Gagal mendapatkan data pengguna", databaseError.toException())
            }
        })
    }


    private fun saveUserToSharedPreferences(tanggal: String) {
        val sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("userTanggal", tanggal)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        // Menampilkan informasi pengguna dari SharedPreferences saat fragment dibuka
        val sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val userTanggal = sharedPreferences.getString("userTanggal", "") ?: ""

        // Menampilkan informasi pengguna ke UI
        userTanggalTextView.text = userTanggal

    }

}