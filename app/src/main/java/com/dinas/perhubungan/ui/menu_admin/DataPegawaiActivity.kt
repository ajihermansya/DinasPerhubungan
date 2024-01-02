package com.dinas.perhubungan.ui.menu_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.dinas.perhubungan.adapter.ChatAdapter
import com.dinas.perhubungan.data.model.JabatanModel
import com.dinas.perhubungan.databinding.ActivityDataPegawaiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataPegawaiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataPegawaiBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var userList: ArrayList<JabatanModel>
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataPegawaiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        userList = ArrayList()
        chatAdapter = ChatAdapter(this, userList)
        binding.userListRecyclerView.adapter = chatAdapter

        database = FirebaseDatabase.getInstance()

        val layoutManager = LinearLayoutManager(this)
        binding.userListRecyclerView.layoutManager = layoutManager

        val databaseReference = database.reference.child("1upeIRUT1x-fPdBdq6X7sM8aSe7QHbCACFvRfROhCnpc").child("data_jabatan")

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (snapshot1 in snapshot.children){
                    val user = snapshot1.getValue(JabatanModel::class.java)
                    user?.let {
                        userList.add(it)
                    }
                }
                chatAdapter.notifyDataSetChanged() // Memperbarui tampilan setelah memperbarui userList
            }

            override fun onCancelled(error: DatabaseError) {
                // Tindakan yang dapat dilakukan saat pembatalan
            }
        }

        databaseReference.addValueEventListener(valueEventListener)
    }
}
