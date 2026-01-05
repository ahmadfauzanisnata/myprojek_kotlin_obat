package com.example.myprojek.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myprojek.data.dao.AppDao
import com.example.myprojek.data.entity.Obat
import com.example.myprojek.data.entity.User

@Database(entities = [User::class, Obat::class], version = 1, exportSchema = false)
abstract class MedicineDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var Instance: MedicineDatabase? = null

        fun getDatabase(context: Context): MedicineDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MedicineDatabase::class.java, "medicine_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
