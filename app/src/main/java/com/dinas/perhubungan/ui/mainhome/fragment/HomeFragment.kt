import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dinas.perhubungan.data.model.UserModel
import com.dinas.perhubungan.databinding.FragmentHomeBinding
import com.dinas.perhubungan.ui.loginregis.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

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
        val databaseReference = database.reference.child("users").child(nip)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Dokumen pengguna ditemukan
                    val userModel = dataSnapshot.getValue(UserModel::class.java)
                    val userName = userModel?.nama_panjang ?: "Nama tidak tersedia"
                    val userNip = userModel?.nip?.replace(Regex("[^0-9]"), "") ?: "Nip tidak tersedia"
                    val userJabatan = userModel?.jabatan ?: "Jabatan tidak tersedia"
                    val userImage = userModel?.imageUrl ?: "Image tidak tersedia"
                    binding.nameUser.text = userName
                    binding.nip.text = userNip
                    binding.jabatan.text = userJabatan

                    // Menyimpan informasi pengguna ke SharedPreferences
                    saveUserToSharedPreferences(userName, userNip, userJabatan, userImage)
                    loadUserFromSharedPreferences()
                } else {
                    Log.d("fetchUserName", "Dokumen pengguna tidak ditemukan")
                    Toast.makeText(requireContext(), "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("fetchUserName", "Gagal mendapatkan data pengguna", databaseError.toException())
                Toast.makeText(requireContext(), "Gagal mendapatkan data pengguna", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserToSharedPreferences(userName: String, userNip: String, userJabatan: String, userImage: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("userName", userName)
        editor.putString("userNip", userNip)
        editor.putString("userJabatan", userJabatan)
        editor.putString("userImage", userImage)
        editor.apply()
    }

    private fun loadUserFromSharedPreferences() {
        val userName = sharedPreferences.getString("userName", "Nama tidak tersedia") ?: "Nama tidak tersedia"
        val userNip = sharedPreferences.getString("userNip", "Nip tidak tersedia") ?: "Nip tidak tersedia"
        val userJabatan = sharedPreferences.getString("userJabatan", "Jabatan tidak tersedia") ?: "Jabatan tidak tersedia"
        val userImage = sharedPreferences.getString("userImage", "Image tidak tersedia") ?: "Image tidak tersedia"

        // Menampilkan informasi pengguna ke UI
        binding.nameUser.text = userName
        binding.nip.text = userNip
        binding.jabatan.text = userJabatan
        Glide.with(requireContext()).load(userImage).into(binding.userImages)
    }
}
