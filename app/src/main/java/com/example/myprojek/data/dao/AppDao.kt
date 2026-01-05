package com.example.myprojek.data.dao

import androidx.room.*
import com.example.myprojek.data.entity.Obat
import com.example.myprojek.data.entity.User
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface AppDao {
    // --- User ---
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.ABORT)
    suspend fun registerUser(user: User)

    @androidx.room.Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUser(email: String): User?

    // --- Obat ---
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertObat(obat: Obat)

    @androidx.room.Update
    suspend fun updateObat(obat: Obat)

    @androidx.room.Delete
    suspend fun deleteObat(obat: Obat)

    @androidx.room.Query("SELECT * FROM obat WHERE userEmail = :email ORDER BY namaObat ASC")
    fun getAllObatByUser(email: String): Flow<List<Obat>>

    @androidx.room.Query("SELECT * FROM obat WHERE id = :id")
    fun getObatById(id: Int): Flow<Obat>
}
