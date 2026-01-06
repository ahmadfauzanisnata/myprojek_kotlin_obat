package com.example.myprojek.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Di Obat.kt, pastikan ada properti ini:
@Entity(tableName = "obat")
data class Obat(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val namaObat: String,
    val dosis: String,
    val frekuensi: String,
    val jamMinum: String,
    val userEmail: String // email pengguna
)
