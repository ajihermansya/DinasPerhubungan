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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
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
                    // Dokumen pengguna ditemukan
                    val userModel = dataSnapshot.getValue(UserModel::class.java)
                    val userName = userModel?.nama_panjang ?: "Nama tidak tersedia"
                    val userNip = userModel?.nip?.replace(Regex("[^0-9]"), "") ?: "Nip tidak tersedia"
                    val userJabatan = userModel?.jabatan ?: "Jabatan tidak tersedia"
                    val userImage = userModel?.imageUrl ?: "Image tidak tersedia"
                    binding.nameUser.text = userName
                    binding.nip.text = userNip
                    binding.jabatan.text = userJabatan
                    binding.userImages
                    //Glide.with(requireContext()).load(userImage).into(imageView)
                    // Menyimpan informasi pengguna ke SharedPreferences
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

    private fun saveUserToSharedPreferences(userName: String, userNip: String, userJabatan: String, userImage: String) {
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
        // Mengambil informasi pengguna dari SharedPreferences saat fragment dibuka
        val sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", "") ?: ""
        val userNip = sharedPreferences.getString("userNip", "") ?: ""
        val userJabatan = sharedPreferences.getString("userJabatan", "") ?: ""
        val userImage = sharedPreferences.getString("userImage", "") ?: ""

        // Menampilkan informasi pengguna ke UI
        binding.nameUser.text = userName
        binding.nip.text = userNip
        binding.jabatan.text = userJabatan
        val imageView = binding.userImages
        Glide.with(requireContext()).load(userImage).into(imageView)
    }
}
