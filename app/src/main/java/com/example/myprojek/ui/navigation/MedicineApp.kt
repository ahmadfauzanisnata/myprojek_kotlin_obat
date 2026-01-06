package com.example.myprojek.ui.navigation

import androidx.compose.runtime.Composable
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
    // ViewModel di-share agar state email/password bisa dikelola
    val authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)

    NavHost(navController = navController, startDestination = Screen.Login.name) {

        // --- LOGIN ---
        composable(Screen.Login.name) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.name) {
                        popUpTo(Screen.Login.name) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.name) },
                onNavigateToForgot = { navController.navigate(Screen.ForgotPassword.name) }
            )
        }

        // --- REGISTER ---
        composable(Screen.Register.name) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    // Setelah register berhasil, arahkan ke Login agar user login manual
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- FORGOT PASSWORD ---
        composable(Screen.ForgotPassword.name) {
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- HOME ---
        composable(Screen.Home.name) {
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
