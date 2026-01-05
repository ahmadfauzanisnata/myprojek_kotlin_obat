package com.example.myprojek.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "obat")
data class Obat(    @PrimaryKey(autoGenerate = true) val id: Int = 0,
                    val namaObat: String,
                    val dosis: String,     // e.g., "1 Tablet"
                    val frekuensi: String, // e.g., "3x Sehari"
                    val jamMinum: String,  // e.g., "08:00" (Untuk alarm sederhana)
                    val userEmail: String  // Foreign Key logic (milik siapa obat ini)
)
