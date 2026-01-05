package com.example.myprojek

import android.app.Application
import com.example.myprojek.data.database.MedicineDatabase
import com.example.myprojek.data.repository.AppRepository

class MyProjekApplication : Application() {
    // Manual DI Container
    lateinit var repository: AppRepository

    override fun onCreate() {
        super.onCreate()
        val database = MedicineDatabase.getDatabase(this)
        repository = AppRepository(database.appDao())
    }
}
