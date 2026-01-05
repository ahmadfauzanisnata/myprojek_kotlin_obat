package com.example.myprojek.ui.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprojek.data.entity.Obat
import com.example.myprojek.data.repository.AppRepository
import com.example.myprojek.worker.AlarmReceiver
import kotlinx.coroutines.launch
import java.util.Calendar

class EntryViewModel(private val repository: AppRepository) : ViewModel() {
    var namaObat by mutableStateOf("")
    var dosis by mutableStateOf("")
    var frekuensi by mutableStateOf("")
    var jam by mutableStateOf("08:00")

    // Menyimpan ID jika mode edit
    private var currentObatId: Int? = null

    // Fungsi untuk memuat data jika Edit
    fun loadObatIfExist(id: Int) {
        if (id != -1 && currentObatId == null) { // Load hanya sekali
            viewModelScope.launch {
                repository.getObatStream(id).collect { obat ->
                    currentObatId = obat.id
                    namaObat = obat.namaObat
                    dosis = obat.dosis
                    frekuensi = obat.frekuensi
                    jam = obat.jamMinum
                }
            }
        }
    }

    suspend fun saveObat(userEmail: String, context: Context) {
        val obat = Obat(
            id = currentObatId ?: 0, // Gunakan ID lama jika edit, 0 jika baru
            namaObat = namaObat,
            dosis = dosis,
            frekuensi = frekuensi,
            jamMinum = jam,
            userEmail = userEmail
        )

        if (currentObatId != null) {
            repository.updateObat(obat) // Update
        } else {
            repository.insertObat(obat) // Insert Baru
        }

        // Reset Alarm dengan data baru
        setAlarm(context, obat.namaObat, jam)
    }

    // ... Fungsi setAlarm tetap sama seperti sebelumnya ...
    private fun setAlarm(context: Context, nama: String, jamStr: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", "Waktunya minum: $nama")
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            nama.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val parts = jamStr.split(":")
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, parts[0].toInt())
            set(Calendar.MINUTE, parts[1].toInt())
            set(Calendar.SECOND, 0)
        }
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } catch (e: SecurityException) { }
    }
}
