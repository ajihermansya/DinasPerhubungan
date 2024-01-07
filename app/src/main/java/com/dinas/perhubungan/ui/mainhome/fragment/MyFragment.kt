package com.dinas.perhubungan.ui.mainhome.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.dinas.perhubungan.R
import com.dinas.perhubungan.data.PrefsManager
import com.dinas.perhubungan.databinding.FragmentMyBinding
import com.dinas.perhubungan.ui.loginregis.LoginActivity
import com.dinas.perhubungan.ui.mainhome.person.AkunActivity
import com.dinas.perhubungan.ui.mainhome.person.EditProfileActivity
import com.dinas.perhubungan.ui.mainhome.person.NotifikasiActivity
import com.dinas.perhubungan.ui.mainhome.person.PrivacyActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MyFragment : Fragment() {
    private lateinit var binding: FragmentMyBinding
    private lateinit var prefsManager: PrefsManager
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyBinding.inflate(inflater, container, false)
        prefsManager = PrefsManager(requireContext())
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val language = binding.root.findViewById<LinearLayout>(R.id.language)
        val notif = binding.root.findViewById<LinearLayout>(R.id.notifikasi)
        val privasi = binding.root.findViewById<LinearLayout>(R.id.ketentuan_privasi)
        val akun = binding.root.findViewById<ImageView>(R.id.btn_akunsaya)
        val edit = binding.root.findViewById<ImageView>(R.id.btn_edit)
        userNameTextView = binding.root.findViewById(R.id.userNames)
        userEmailTextView = binding.root.findViewById(R.id.text_email)
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



}