package com.example.myprojek.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.myprojek.data.entity.Obat

import com.example.myprojek.data.repository.AppRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AppRepository) : ViewModel() {

    // Simpan daftar asli dari database
    private val _originalList = MutableStateFlow<List<Obat>>(emptyList())

    // State untuk Search Query
    var searchQuery by mutableStateOf("")
        private set

    // State UI yang sudah difilter
    private val _homeUiState = MutableStateFlow<List<Obat>>(emptyList())
    val homeUiState: StateFlow<List<Obat>> = _homeUiState

    fun getObatList(userEmail: String) {
        viewModelScope.launch {
            repository.getAllObat(userEmail).collect { list ->
                _originalList.value = list
                filterList() // Filter awal
            }
        }
    }

    // Fungsi update query dari UI
    fun updateSearchQuery(query: String) {
        searchQuery = query
        filterList()
    }

    // Logika Filter
    private fun filterList() {
        if (searchQuery.isBlank()) {
            _homeUiState.value = _originalList.value
        } else {
            _homeUiState.value = _originalList.value.filter {
                it.namaObat.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    // Di HomeViewModel.kt, tambahkan fungsi ini:
    fun getTodayReminderCount(obatList: List<Obat>): Int {
        // Logika untuk menghitung berapa reminder hari ini
        // Untuk sementara, return count semua obat
        return obatList.size
    }

    suspend fun deleteObat(obat: Obat) {
        repository.deleteObat(obat)
    }
}
