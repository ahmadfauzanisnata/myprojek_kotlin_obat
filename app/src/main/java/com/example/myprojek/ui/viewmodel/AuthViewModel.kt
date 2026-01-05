package com.example.myprojek.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprojek.data.entity.User
import com.example.myprojek.data.repository.AppRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AppRepository) : ViewModel() {
    var emailInput by mutableStateOf("")
    var passwordInput by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)
    var isLoggedIn by mutableStateOf(false)
    var currentUserEmail by mutableStateOf("")

    fun login() {
        viewModelScope.launch {
            val user = repository.login(emailInput)
            if (user != null && user.passwordHash == passwordInput) {
                isLoggedIn = true
                currentUserEmail = user.email
                errorMessage = null
            } else {
                errorMessage = "Email atau Password salah"
            }
        }
    }

    fun register() {
        viewModelScope.launch {
            try {
                repository.register(User(emailInput, passwordInput, "User"))
                errorMessage = "Registrasi Berhasil! Silakan Login."
            } catch (e: Exception) {
                errorMessage = "Registrasi Gagal (Email mungkin sudah ada)."
            }
        }
    }

    fun logout() {
        isLoggedIn = false
        currentUserEmail = ""
        emailInput = ""
        passwordInput = ""
    }
}
