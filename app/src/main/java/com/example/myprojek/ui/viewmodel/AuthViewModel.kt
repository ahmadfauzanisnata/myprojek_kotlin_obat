package com.example.myprojek.ui.viewmodel

import android.util.Patterns
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
    var confirmPasswordInput by mutableStateOf("") // Untuk Register

    var errorMessage by mutableStateOf<String?>(null)
    var registerSuccessMessage by mutableStateOf<String?>(null) // PESAN SUKSES KHUSUS REGISTER
    var resetPasswordSuccessMessage by mutableStateOf<String?>(null) // PESAN SUKSES KHUSUS RESET PASSWORD

    var isLoggedIn by mutableStateOf(false)
    var currentUserEmail by mutableStateOf("")

    // --- Validasi ---
    fun validateInput(): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            errorMessage = "Format email tidak valid"
            return false
        }
        if (passwordInput.length <= 5) {
            errorMessage = "Password harus lebih dari 5 karakter"
            return false
        }
        return true
    }

    // --- Login ---
    fun login() {
        // Reset pesan error sebelum mencoba login
        errorMessage = null
        registerSuccessMessage = null
        resetPasswordSuccessMessage = null

        if (!validateInput()) return

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

    // --- Register --- (DIPERBAIKI)
    fun register(onSuccess: () -> Unit) {
        errorMessage = null
        registerSuccessMessage = null

        if (!validateInput()) return
        if (passwordInput != confirmPasswordInput) {
            errorMessage = "Password konfirmasi tidak sama"
            return
        }

        viewModelScope.launch {
            try {
                // Cek dulu apakah email sudah ada
                val existingUser = repository.login(emailInput)
                if (existingUser != null) {
                    errorMessage = "Email sudah terdaftar"
                } else {
                    repository.register(User(emailInput, passwordInput, "User"))
                    errorMessage = null

                    // SET PESAN SUKSES KHUSUS REGISTER
                    registerSuccessMessage = "Akun berhasil dibuat! Anda dapat login sekarang."

                    // Bersihkan password, tapi biarkan email agar user mudah login
                    passwordInput = ""
                    confirmPasswordInput = ""

                    // JANGAN PANGGIL onSuccess() OTOMATIS
                    // Biarkan user membaca pesan sukses dulu
                    // onSuccess() akan dipanggil manual dari UI nanti
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan saat registrasi"
            }
        }
    }

    // --- Reset Password --- (DIPERBAIKI)
    fun resetPassword(onSuccess: () -> Unit) {
        // 1. Reset state pesan
        errorMessage = null
        resetPasswordSuccessMessage = null

        if (!validateInput()) return

        viewModelScope.launch {
            // 2. Cek user
            val user = repository.login(emailInput)
            if (user != null) {
                // 3. Update password
                repository.resetPassword(emailInput, passwordInput)

                // 4. Set pesan sukses KHUSUS RESET PASSWORD
                resetPasswordSuccessMessage = "Password berhasil diubah. Anda dapat login dengan password baru."
                errorMessage = null

                // 5. Bersihkan input password
                passwordInput = ""
                confirmPasswordInput = ""

                // JANGAN panggil onSuccess() otomatis
                // Biarkan user membaca pesan sukses dulu
            } else {
                errorMessage = "Email tidak ditemukan"
                resetPasswordSuccessMessage = null
            }
        }
    }

    fun logout() {
        isLoggedIn = false
        currentUserEmail = ""
        clearAllMessages()
        clearFields()
    }

    // Fungsi untuk reset semua messages
    fun clearAllMessages() {
        errorMessage = null
        registerSuccessMessage = null
        resetPasswordSuccessMessage = null
    }

    // Fungsi untuk clear semua fields
    fun clearFields() {
        emailInput = ""
        passwordInput = ""
        confirmPasswordInput = ""
        clearAllMessages()
    }

    // Fungsi khusus untuk reset setelah register sukses
    fun afterRegisterSuccess() {
        emailInput = "" // Kosongkan email juga
        passwordInput = ""
        confirmPasswordInput = ""
        registerSuccessMessage = null // Reset pesan sukses register
    }

    // Fungsi khusus untuk reset setelah reset password sukses
    fun afterResetPasswordSuccess() {
        passwordInput = ""
        confirmPasswordInput = ""
        resetPasswordSuccessMessage = null // Reset pesan sukses reset password
    }
}