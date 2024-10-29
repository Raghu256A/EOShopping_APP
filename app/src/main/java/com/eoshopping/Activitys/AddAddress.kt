package com.eoshopping.Activitys

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.eoshopping.AccountFragment
import com.eoshopping.R
import com.eoshopping.common_utils.CommonUtil
import com.eoshopping.pojo.AddressDo
import com.eoshopping.repository.UserRepository
import com.eoshopping.signpages.SignInActivity
import com.eoshopping.viewModel.UserViewModel
import com.eoshopping.viewModelFactory.UserViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import java.lang.Exception

class AddAddress :AppCompatActivity() {
    private lateinit var chck_confirm:CheckBox
    private lateinit var add_type_h :Button
    private lateinit var add_type_c:Button
    private lateinit var add_type_o:Button
    private lateinit var et_Name:TextInputEditText
    private lateinit var et_mobileNumber:TextInputEditText
    private lateinit var et_country:TextInputEditText
    private lateinit var et_state:TextInputEditText
    private lateinit var et_pinecode:TextInputEditText
    private lateinit var et_townOrCity:TextInputEditText
    private lateinit var et_landmark:TextInputEditText
    private lateinit var et_Area:TextInputEditText
    private lateinit var et_fullAddress:TextInputEditText
    private lateinit var bnt_addAddress:Button
    private lateinit var cancel_button:Button
    private var isDefaultAdd: String?=""
    private var addressType:String?=""
    private var name:String?=""
    private var mobileNumber:String?=""
    private var country:String?=""
    private var state:String?=""
    private var pineCode:String?=""
    private var townOrCity:String?=""
    private var landMark:String?=""
    private var area:String?=""
    private var fullAddress:String?=""
    private var userMobile:String?=""
    private var userEmail:String?=""
    private var deliveryInstructions:String?=""
    private var type_h:Boolean?=true
    private var type_c:Boolean?=true
    private var type_o:Boolean?=true
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_address_details)
         val window:Window =window
        window.setBackgroundDrawableResource(R.drawable.popup_layout_view)
        this.setFinishOnTouchOutside(false)
        updateXML()
        saveAddressStatus()
        val intent = intent
        userEmail = intent.getStringExtra("UEmail")
        userMobile = intent.getStringExtra("UMobileNumber")


    }
    private fun updateXML(){
        try{
            chck_confirm = findViewById(R.id.chck_confirm)
            add_type_h = findViewById(R.id.add_type_h)
            add_type_c = findViewById(R.id.add_type_c)
            add_type_o = findViewById(R.id.add_type_o)
            et_Name = findViewById(R.id.et_Name)
            et_mobileNumber = findViewById(R.id.et_mobileNumber)
            et_country = findViewById(R.id.et_Country)
            et_state = findViewById(R.id.et_State)
            et_pinecode = findViewById(R.id.et_pinecode)
            et_townOrCity = findViewById(R.id.et_townOrCity)
            et_landmark = findViewById(R.id.et_landmark)
            et_Area = findViewById(R.id.et_Area)
            et_fullAddress = findViewById(R.id.et_fullAddress)
            bnt_addAddress = findViewById(R.id.bnt_addAddress)
            cancel_button=findViewById(R.id.cancel_button)
            bnt_addAddress.setOnClickListener(View.OnClickListener {
               val alert = getAlerts()
                if (alert.isNullOrEmpty()){
                    saveAddress()
                }else{
                    CommonUtil.showAlertDialog(this,"Alert",alert)
                }
            })
            try{
            cancel_button.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })
            }catch (e:Exception){
               e.printStackTrace()
            }

            add_type_h.setOnClickListener(View.OnClickListener {
                if (type_h==true){
                    add_type_h.isEnabled=false
                    type_h=false
                    if (type_c==false){
                        add_type_c.isEnabled=true
                        type_c=true
                    }
                    if (type_o==false){
                        add_type_o.isEnabled=true
                        type_o=true
                    }
                }
            })
            add_type_o.setOnClickListener(View.OnClickListener {
                if (type_o==true){
                    add_type_o.isEnabled=false
                    type_o=false
                    if (type_h==false){
                        add_type_h.isEnabled=true
                        type_h=true
                    }
                    if (type_c==false){
                        add_type_c.isEnabled=true
                        type_c=true
                    }
                }
            })
            add_type_c.setOnClickListener(View.OnClickListener {
                if (type_c==true){
                    add_type_c.isEnabled=false
                    type_c=false
                    if (type_h==false){
                        add_type_h.isEnabled=true
                        type_h=true
                    }
                    if (type_o==false){
                        add_type_o.isEnabled=true
                        type_o=true
                    }
                }
            })
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun getAlerts(): String? {
        var alert: String? = ""
        name = et_Name.text.toString()
        mobileNumber=et_mobileNumber.text.toString()
        country=et_country.text.toString()
        state=et_state.text.toString()
        pineCode=et_pinecode.text.toString()
        area=et_Area.text.toString()
        fullAddress=et_fullAddress.text.toString()
        townOrCity=et_townOrCity.text.toString()
        landMark=et_landmark.text.toString()
        isDefaultAdd = if (chck_confirm.isChecked) "1" else "0"
        if (type_c==false){
            addressType="Company"
        }
        if (type_h==false){
            addressType="House"
        }
        if (type_o==false){
            addressType="Other"
        }
        if (type_c==true&&type_h==true&&type_o==true){
            alert += "address type should not be empty\n"
        }
        if (name.isNullOrEmpty()) {
            alert += "name should not be empty\n"
        }
        if (mobileNumber.isNullOrEmpty()) {
            alert += "mobile number should not be empty\n"
        }
        if (pineCode.isNullOrEmpty()) {
            alert += "pinCode should not be empty\n"
        }
        if (pineCode!!.length!=6) {
            alert += "pinCode length must be 6 \n"
        }
        if (country.isNullOrEmpty()){
            alert+="country should not be empty\n"
        }
        if (state.isNullOrEmpty()){
            alert+="state should not be empty\n"
        }
        if (area.isNullOrEmpty()){
            alert+="area should not be empty"
        }
        if (townOrCity.isNullOrEmpty()){
            alert+="Town/City should not be empty"
        }
        if (landMark.isNullOrEmpty()){
            alert+="landMark should not be empty"
        }
        if (fullAddress.isNullOrEmpty()){
            alert+="Flat,House no should not be empty"
        }
        if (alert.isNullOrEmpty()){
            alert+= mobileNumber?.let { CommonUtil.isMobileValid(it,"") }
        }

        return alert
    }
    private fun saveAddressStatus(){
        val repository= UserRepository()
        viewModel = ViewModelProvider(this, UserViewModelFactory(repository)).get(UserViewModel::class.java)

        viewModel.address.observe(this){status->
            if (status.first){
                Toast.makeText(this, "Address added Successful", Toast.LENGTH_LONG).show()
                onBackPressed()
            }else{
                CommonUtil.showAlertDialog(this,"Alert","Address added Failed")

            }
        }
    }
    private fun saveAddress(){
        try {
            val address =AddressDo(IsDefaultAdd =isDefaultAdd!!, AddressType = addressType!!, Name = name!!,
                MobileNumber =mobileNumber!!, Country = country!!, State = state!!, PineCode = pineCode!!, TownOrCity = townOrCity!!,
            LandMark = landMark!!, Area = area!!, FullAddress = fullAddress!!, UserMobile = userMobile!!, UserEmail = userEmail!!, DeliveryInstructions = "")
      viewModel.saveAddress(address,userMobile,userEmail!!)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()

        finish()
    }
}