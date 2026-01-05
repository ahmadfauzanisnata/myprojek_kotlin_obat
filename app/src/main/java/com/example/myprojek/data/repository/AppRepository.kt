package com.example.myprojek.data.repository

import com.example.myprojek.data.dao.AppDao
import com.example.myprojek.data.entity.Obat
import com.example.myprojek.data.entity.User
import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {
    // User logic
    suspend fun register(user: User) = appDao.registerUser(user)
    suspend fun login(email: String) = appDao.getUser(email)

    // Obat logic
    fun getAllObat(email: String): Flow<List<Obat>> = appDao.getAllObatByUser(email)
    fun getObatStream(id: Int): Flow<Obat> = appDao.getObatById(id)
    suspend fun insertObat(obat: Obat) = appDao.insertObat(obat)
    suspend fun deleteObat(obat: Obat) = appDao.deleteObat(obat)
    suspend fun updateObat(obat: Obat) = appDao.updateObat(obat)
}
