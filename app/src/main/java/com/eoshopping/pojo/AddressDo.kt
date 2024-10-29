package com.eoshopping.pojo

data class AddressDo(
    var id:String?=null,
    var IsDefaultAdd: String="",
    var AddressType:String="",
    var Name:String="",
    var MobileNumber:String="",
    var Country:String="",
    var State:String="",
    var PineCode:String="",
    var TownOrCity:String="",
    var LandMark:String="",
    var Area:String="",
    var FullAddress:String="",
    var UserMobile:String="",
    var UserEmail:String="",
    var DeliveryInstructions:String=""
)
