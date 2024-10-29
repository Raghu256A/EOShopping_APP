package com.eoshopping.viewModel

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eoshopping.pojo.AddressDo
import com.eoshopping.pojo.UserDo
import com.eoshopping.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginStatus = MutableLiveData<Pair<Boolean, String?>>()
    val loginStatus: LiveData<Pair<Boolean, String?>> = _loginStatus
    private val _userStatus = MutableLiveData<UserDo?>()
    val userStatus: LiveData<UserDo?> = _userStatus
    private val _address = MutableLiveData<Pair<Boolean, String?>>()
    val address: LiveData<Pair<Boolean, String?>> = _address
    private val _addressList = MutableLiveData<List<AddressDo>>()
    val addressList: LiveData<List<AddressDo>> = _addressList


    fun registerUser(user: UserDo, profilePic: Uri?) {
        repository.registerUser(user, profilePic) { success, message ->
            _loginStatus.postValue(Pair(success, message) as Pair<Boolean, String?>?)
        }
    }

    fun loginWithEmail(email: String, password: String) {
        repository.loginWithEmail(email, password) { success, messsage ->
            _loginStatus.postValue(Pair(success, messsage))
        }
    }

    fun loginWithMobile(Mobile: String, password: String) {
        repository.loginWithMobileNumber(Mobile, password) { success, messsage ->
            _loginStatus.postValue(Pair(success, messsage))
        }
    }

    fun getUserData(userId: String) {
        viewModelScope.launch {
            _userStatus.value = repository.getUserData(userId)
        }
    }

    fun saveAddress(address: AddressDo, userIdM: String?,userIdE: String) {
        viewModelScope.launch {
            repository.saveUserAddress(userIdM,userIdE, address) { success, message ->
                _address.postValue(Pair(success, message))
            }
        }

    }
    fun getAddress(userIdM: String?,userIdE: String?){
        viewModelScope.launch {
            val addressList=repository.getAddress(userIdM,userIdE)
            _addressList.value=addressList
        }
    }
}