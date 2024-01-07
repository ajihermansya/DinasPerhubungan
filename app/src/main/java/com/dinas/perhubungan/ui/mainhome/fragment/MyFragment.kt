import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dinas.perhubungan.R
import com.dinas.perhubungan.data.PrefsManager
import com.dinas.perhubungan.data.model.UserModel
import com.dinas.perhubungan.databinding.FragmentMyBinding
import com.dinas.perhubungan.ui.loginregis.LoginActivity
import com.dinas.perhubungan.ui.mainhome.person.NotifikasiActivity
import com.dinas.perhubungan.ui.mainhome.person.PrivacyActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyFragment : Fragment() {
    private lateinit var binding: FragmentMyBinding
    private lateinit var prefsManager: PrefsManager
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var userjabatanTextView: TextView
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBinding.inflate(inflater, container, false)
        prefsManager = PrefsManager(requireContext())
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Initialize views and set click listeners
        initViews()

        return binding.root
    }

    private fun initViews() {
        val language = binding.root.findViewById<LinearLayout>(R.id.language)
        val notif = binding.root.findViewById<LinearLayout>(R.id.notifikasi)
        val privasi = binding.root.findViewById<LinearLayout>(R.id.ketentuan_privasi)
        val akun = binding.root.findViewById<ImageView>(R.id.btn_akunsaya)
        val edit = binding.root.findViewById<ImageView>(R.id.btn_edit)
        userNameTextView = binding.root.findViewById(R.id.userNames)
        userEmailTextView = binding.root.findViewById(R.id.text_email)
        userjabatanTextView = binding.root.findViewById(R.id.jabatan_person)
        imageView = binding.root.findViewById(R.id.userImage)

        val keluarButton = binding.root.findViewById<Button>(R.id.riwayatKonseling)

        language.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        notif.setOnClickListener {
            startActivity(Intent(requireContext(), NotifikasiActivity::class.java))
        }

        privasi.setOnClickListener {
            startActivity(Intent(requireContext(), PrivacyActivity::class.java))
        }

        akun.setOnClickListener {
            showUnderDevelopmentDialog(requireContext())
        }

        edit.setOnClickListener {
            showUnderDevelopmentDialog(requireContext())
        }

        keluarButton.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        prefsManager.token = ""
        prefsManager.userEmail = ""
        prefsManager.isExampleLogin = false
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showUnderDevelopmentDialog(context: Context) {
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

                    userNameTextView.text = userName
                    userEmailTextView.text = userNip
                    userjabatanTextView.text = userJabatan
                    Glide.with(requireContext()).load(userImage).into(imageView)

                    saveUserToSharedPreferences(userName, userNip, userJabatan, userImage)
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
        val sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", "") ?: ""
        val userNip = sharedPreferences.getString("userNip", "") ?: ""
        val userJabatan = sharedPreferences.getString("userJabatan", "") ?: ""
        val userImage = sharedPreferences.getString("userImage", "") ?: ""

        userNameTextView.text = userName
        userEmailTextView.text = userNip
        userjabatanTextView.text = userJabatan
        Glide.with(requireContext()).load(userImage).into(imageView)
    }
}
