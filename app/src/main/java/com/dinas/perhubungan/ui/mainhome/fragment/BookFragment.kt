package com.dinas.perhubungan.ui.mainhome.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dinas.perhubungan.adapter.ChatAdapter
import com.dinas.perhubungan.data.model.JabatanModel
import com.dinas.perhubungan.databinding.FragmentBookBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import timber.log.Timber

class BookFragment : Fragment() {
    private lateinit var binding: FragmentBookBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var userList: ArrayList<JabatanModel>
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var originalUserList: ArrayList<JabatanModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(inflater, container, false)
        userList = ArrayList()
        chatAdapter = ChatAdapter(requireContext(), userList)
        binding.userListRecyclerView.adapter = chatAdapter

        database = FirebaseDatabase.getInstance()

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    performSearch(it)
                }
                return true
            }
        })


        val layoutManager = LinearLayoutManager(requireContext())
        binding.userListRecyclerView.layoutManager = layoutManager

        val databaseReference = database.reference.child("1upeIRUT1x-fPdBdq6X7sM8aSe7QHbCACFvRfROhCnpc")
            .child("data_jabatan")
        showLoading(true)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                showLoading(true)
                userList.clear()
                originalUserList = ArrayList() // Inisialisasi originalUserList
                for (snapshot1 in snapshot.children){
                    val user = snapshot1.getValue(JabatanModel::class.java)
                    user?.let {
                        userList.add(it)
                        originalUserList.add(it)
                    }
                }
                chatAdapter.notifyDataSetChanged()
                if (userList.isEmpty()) {
                    Timber.tag("DataPegawaiActivity").d("Tidak ada data yang ditemukan")
                    Toast.makeText(requireContext(), "Data pegawai tidak ditemukan!!", Toast.LENGTH_SHORT).show()
                }
                showLoading(false)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("DataPegawaiActivity", "Gagal mengambil data: ${error.message}")
            }
        }
        databaseReference.addValueEventListener(valueEventListener)


        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun performSearch(query: String) {
        val filteredList = originalUserList.filter { user ->
            user.nama?.contains(query, ignoreCase = true) == true
        }
        userList.clear()
        userList.addAll(filteredList)
        chatAdapter.notifyDataSetChanged()
    }


}