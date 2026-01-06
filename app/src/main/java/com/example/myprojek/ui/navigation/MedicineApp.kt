package com.example.myprojek.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myprojek.ui.view.auth.ForgotPasswordScreen
import com.example.myprojek.ui.view.auth.LoginScreen
import com.example.myprojek.ui.view.auth.RegisterScreen
import com.example.myprojek.ui.view.entry.EntryScreen
import com.example.myprojek.ui.view.home.HomeScreen
import com.example.myprojek.ui.viewmodel.AuthViewModel
import com.example.myprojek.ui.viewmodel.EntryViewModel
import com.example.myprojek.ui.viewmodel.HomeViewModel

import com.example.myprojek.uiimport.AppViewModelProvider

enum class Screen {
    Login, Register, ForgotPassword, Home, Entry
}

@Composable
fun MedicineApp() {
    val navController = rememberNavController()

    // PERBAIKAN DI SINI: Gunakan factory dari AppViewModelProvider
    val authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)

    // Atau jika menggunakan viewModel() tanpa parameter factory juga bisa
    // val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Login.name) {

        // --- LOGIN ---
        composable(Screen.Login.name) {
            // Effect untuk mengambil email dari ViewModel saat screen muncul
            LaunchedEffect(Unit) {
                // Cek apakah ada email yang disimpan dari halaman lain
                val email = authViewModel.getAndClearLastSuccessfulEmail()
                if (!email.isNullOrEmpty()) {
                    authViewModel.emailInput = email
                }
            }

            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.name) {
                        popUpTo(Screen.Login.name) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    authViewModel.clearFields()
                    navController.navigate(Screen.Register.name)
                },
                onNavigateToForgot = {
                    authViewModel.clearFields()
                    navController.navigate(Screen.ForgotPassword.name)
                }
            )
        }

        // --- REGISTER ---
        composable(Screen.Register.name) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    // Setelah register berhasil, biarkan user baca pesan sukses
                },
                onNavigateBack = {
                    if (authViewModel.registerSuccessMessage == null) {
                        authViewModel.clearFields()
                    }
                    navController.popBackStack()
                }
            )
        }

        // --- FORGOT PASSWORD ---
        composable(Screen.ForgotPassword.name) {
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onNavigateBack = {
                    if (authViewModel.resetPasswordSuccessMessage == null) {
                        authViewModel.clearFields()
                    }
                    navController.popBackStack()
                }
            )
        }

        // --- HOME ---
        composable(Screen.Home.name) {
            // PERBAIKAN DI SINI: Juga gunakan factory untuk HomeViewModel
            val homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
            HomeScreen(
                viewModel = homeViewModel,
                currentUserEmail = authViewModel.currentUserEmail,
                onNavigateToEntry = { navController.navigate("entry_route") },
                onNavigateToEdit = { obatId ->
                    navController.navigate("entry_route?obatId=$obatId")
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.name) {
                        popUpTo(Screen.Home.name) { inclusive = true }
                    }
                }
            )
        }

        // --- ENTRY (ADD/EDIT) ---
        composable(
            route = "entry_route?obatId={obatId}",
            arguments = listOf(navArgument("obatId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            // PERBAIKAN DI SINI: Juga gunakan factory untuk EntryViewModel
            val entryViewModel: EntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val obatId = backStackEntry.arguments?.getInt("obatId") ?: -1

            EntryScreen(
                viewModel = entryViewModel,
                currentUserEmail = authViewModel.currentUserEmail,
                obatId = obatId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}