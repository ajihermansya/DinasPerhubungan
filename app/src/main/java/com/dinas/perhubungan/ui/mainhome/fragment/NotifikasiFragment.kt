package com.dinas.perhubungan.ui.mainhome.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.dinas.perhubungan.R
import com.dinas.perhubungan.databinding.FragmentHomeBinding
import com.dinas.perhubungan.databinding.FragmentNotifikasiBinding


class NotifikasiFragment : Fragment() {
    private lateinit var binding: FragmentNotifikasiBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotifikasiBinding.inflate(inflater, container, false)

        val notif = binding.root.findViewById<LinearLayout>(R.id.notif_switch)
        val tanggal = binding.root.findViewById<LinearLayout>(R.id.tanggal_notif )

        return binding.root
    }


}