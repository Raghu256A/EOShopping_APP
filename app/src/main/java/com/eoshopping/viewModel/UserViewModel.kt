package com.eoshopping.viewModel

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eoshopping.pojo.UserDo
import com.eoshopping.repository.UserRepository

class UserViewModel(private val repository: UserRepository):ViewModel() {
        private val _loginStatus=MutableLiveData<Pair<Boolean,String?>>()
    val loginStatus:LiveData<Pair<Boolean,String?>> =_loginStatus


    fun registerUser(user:UserDo,profilePic: Uri?){
        repository.registerUser(user,profilePic){success,message ->
            _loginStatus.postValue(Pair(success,message) as Pair<Boolean, String?>?)
        }
    }
    fun loginWithEmail(email:String,password:String){
        repository.loginWithEmail(email,password){success,messsage ->
            _loginStatus.postValue(Pair(success,messsage))
        }
    }
    fun loginWithMobile(Mobile:String,password:String){
        repository.loginWithMobileNumber(Mobile,password){success,messsage ->
            _loginStatus.postValue(Pair(success,messsage))
        }
    }

}