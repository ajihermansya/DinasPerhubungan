package com.dinas.perhubungan.data.model

data class UserModel(
    val nip : String?="",
    val uid : String?="",
    val nama_panjang : String?="",
    val jabatan : String?="",
    val tanggal : String?="",
    val tlpn : String?="",
    val password : String?="",
    val imageUrl : String?="",
)
