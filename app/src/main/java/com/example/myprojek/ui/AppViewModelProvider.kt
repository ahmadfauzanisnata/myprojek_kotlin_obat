package com.example.myprojek.uiimport

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myprojek.MyProjekApplication
import com.example.myprojek.ui.viewmodel.AuthViewModel
import com.example.myprojek.ui.viewmodel.HomeViewModel
import com.example.myprojek.ui.viewmodel.EntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AuthViewModel(myProjekApplication().repository)
        }
        initializer {
            HomeViewModel(myProjekApplication().repository)
        }
        initializer {
            EntryViewModel(myProjekApplication().repository)
        }
    }
}

fun CreationExtras.myProjekApplication(): MyProjekApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyProjekApplication)
