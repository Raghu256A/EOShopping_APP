package com.eoshopping.pojo

data class UserDo(
    val fullName: String = "",
    val mobileNumber: String = "",
    val email: String = "",
    val password: String = "",
    val profileImageUrl: String? = ""
)
