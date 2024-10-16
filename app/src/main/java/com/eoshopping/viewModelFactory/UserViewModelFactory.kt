package com.eoshopping.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eoshopping.repository.UserRepository
import com.eoshopping.viewModel.UserViewModel

class UserViewModelFactory(private val repository: UserRepository) :ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelclass:Class<T>):T{
        if (modelclass.isAssignableFrom(UserViewModel::class.java)){
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}