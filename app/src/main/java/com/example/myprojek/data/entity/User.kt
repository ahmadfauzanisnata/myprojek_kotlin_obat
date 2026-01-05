package com.example.myprojek.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    val passwordHash: String, // Simpan password sederhana saja untuk demo offline
    val nama: String
)
