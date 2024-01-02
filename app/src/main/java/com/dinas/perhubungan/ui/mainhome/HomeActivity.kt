package com.dinas.perhubungan.ui.mainhome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.dinas.perhubungan.R
import com.dinas.perhubungan.data.PrefsManager
import com.dinas.perhubungan.databinding.ActivityHomeBinding
import com.dinas.perhubungan.ui.mainhome.fragment.BookFragment
import com.dinas.perhubungan.ui.mainhome.fragment.HomeFragment
import com.dinas.perhubungan.ui.mainhome.fragment.MyFragment
import com.dinas.perhubungan.ui.mainhome.fragment.NotifikasiFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mAuth : FirebaseAuth
    private lateinit var prefsManager: PrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        prefsManager = PrefsManager(this)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottom_notif -> {
                    replaceFragment(NotifikasiFragment())
                    true
                }
                R.id.bottom_bok -> {
                    replaceFragment(BookFragment())
                    true
                }
                R.id.bottom_my -> {
                    replaceFragment(MyFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeFragment())


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun replaceFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }

}