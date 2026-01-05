package com.example.jadwalkuliahreminder

data class ApiResponse(
    val success: Boolean,
    val message: String? = null,
    val data: List<MataKuliah>? = null,
    val id_jadwal: Int? = null
)

// Update data class MataKuliah agar compatible dengan JSON
