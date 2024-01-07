package com.dinas.perhubungan.ui.mainhome.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dinas.perhubungan.adapter.ListUserAdapter
import com.dinas.perhubungan.data.model.JabatanModel
import com.dinas.perhubungan.databinding.FragmentBookBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.firebase.database.*

class BookFragment : Fragment() {
    private lateinit var binding: FragmentBookBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var userList: ArrayList<JabatanModel>
    private lateinit var listUserAdapter: ListUserAdapter
    private lateinit var originalUserList: ArrayList<JabatanModel>
    private lateinit var sharedPreferences: SharedPreferences

    private val PREFS_NAME = "MyPrefs" // Nama SharedPreferences
    private val USER_LIST_KEY = "UserListKey"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(inflater, container, false)
        userList = ArrayList()
        listUserAdapter = ListUserAdapter(requireContext(), userList)
        binding.userListRecyclerView.adapter = listUserAdapter

        database = FirebaseDatabase.getInstance()
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        setupSearchView()
        setupRecyclerView()

        loadUsersFromFirebase()
        loadUserListFromSharedPreferences()

        return binding.root
    }

    private fun setupSearchView() {
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
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.userListRecyclerView.layoutManager = layoutManager
    }

    private fun loadUsersFromFirebase() {
        showLoading(true)
        val databaseReference = database.reference.child("1upeIRUT1x-fPdBdq6X7sM8aSe7QHbCACFvRfROhCnpc")
            .child("data_jabatan")

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                originalUserList = ArrayList()
                for (snapshot1 in snapshot.children){
                    val user = snapshot1.getValue(JabatanModel::class.java)
                    user?.let {
                        userList.add(it)
                        originalUserList.add(it)
                        showLoading(false)
                    }
                }
                listUserAdapter.notifyDataSetChanged()
                if (userList.isEmpty()) {
                    Toast.makeText(requireContext(), "Data pegawai tidak ditemukan!!", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                showLoading(false)
                Log.e("DataPegawaiActivity", "Gagal mengambil data: ${error.message}")
            }
        }
        databaseReference.addListenerForSingleValueEvent(valueEventListener)
    }

    private fun loadUserListFromSharedPreferences() {
        val gson = Gson()
        val json = sharedPreferences.getString(USER_LIST_KEY, "")
        val type = object : TypeToken<ArrayList<JabatanModel>>() {}.type
        originalUserList = gson.fromJson(json, type) ?: ArrayList()
    }


    private fun performSearch(query: String) {
        val filteredList = originalUserList.filter { user ->
            user.nama?.contains(query, ignoreCase = true) == true
        }
        userList.clear()
        userList.addAll(filteredList)
        listUserAdapter.notifyDataSetChanged()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}