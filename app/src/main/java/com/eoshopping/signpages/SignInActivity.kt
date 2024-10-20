package com.eoshopping.signpages

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.eoshopping.MainActivity
import com.eoshopping.R
import com.eoshopping.common_utils.CommonUtil
import com.eoshopping.common_utils.Constants
import com.eoshopping.pojo.UserDo
import com.eoshopping.repository.UserRepository
import com.eoshopping.runtime_permission.RunTimePermissions
import com.eoshopping.viewModel.UserViewModel
import com.eoshopping.viewModelFactory.UserViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp

class SignInActivity : AppCompatActivity() {
    private var userId: String? = ""
    private var password: String? = ""
    private lateinit var et_UserId: TextInputEditText
    private lateinit var et_password: TextInputEditText
    private lateinit var btn_signIn: Button
    private lateinit var tv_signUp: TextView
    private lateinit var tv_forgotPassword: TextView
    private var isPasswordVisible = false
    private lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        updateXML()
        RunTimePermissions.checkAllPermissions(this)

        val repository= UserRepository()
        userViewModel = ViewModelProvider(this, UserViewModelFactory(repository)).get(UserViewModel::class.java)
        userViewModel.loginStatus.observe(this){status->
            if (status.first){
                Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()
                var intent = Intent(this,MainActivity::class.java)
                intent.putExtra("UserId",userId)
                startActivity(intent)
            }else{
                CommonUtil.showAlertDialog(this,"Alert","Login Failed "+status.second)

            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    fun updateXML() {

        et_UserId = findViewById<TextInputEditText>(R.id.et_userId)
        et_password = findViewById<TextInputEditText>(R.id.et_password)
        btn_signIn = findViewById<Button>(R.id.btn_signin)
        tv_signUp = findViewById<TextView>(R.id.tv_signUp)
        tv_forgotPassword = findViewById<TextView>(R.id.tv_forgotPassword)


        btn_signIn.setOnClickListener(View.OnClickListener {
            userId = et_UserId?.text.toString().trim()
            password = et_password?.text.toString().trim()
            var alerts: String? = getAlerts(userId!!, password!!)
            if (alerts.isNullOrEmpty()) {
               if (isLoginWithEmail(userId!!)==true) {
                   userViewModel.loginWithEmail(userId!!,password!!)
               }else{
                   userViewModel.loginWithMobile(userId!!,password!!)
               }

            } else {
                CommonUtil.showAlertDialog(this, "Alert", alerts)
            }

        })
        tv_signUp.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))

        })
        tv_forgotPassword.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))

        })

        et_password.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // Drawable end index
                if (event.rawX >= et_password.right - et_password.compoundDrawables[drawableEnd].bounds.width()) {
                    isPasswordVisible =
                        CommonUtil.togglePasswordVisibility(isPasswordVisible, et_password)
                    return@setOnTouchListener true
                }
            }
            false
        }
    }


    fun getAlerts(userId: String, password: String): String? {
        var alert: String? = ""
        if (userId.isNullOrEmpty()) {
            alert += "Please Enter UserId\n"
        }
        if (password.isNullOrEmpty()) {
            alert += "Please Enter Password\n"

        }
        if (alert.isNullOrEmpty()) {

            if(isLoginWithEmail(userId) == true) {
                alert += userId?.let { CommonUtil.isValidEmail(it) }
            } else {
                alert += userId?.let { CommonUtil.isMobileValid(it, "") }

            }
            alert += password?.let { CommonUtil.isPasswordValid(it, "") }
        }

        return alert
    }
    fun isLoginWithEmail(userId: String):Boolean?{
        var value:Boolean=false
        val hasCharU = Regex("[a-z]")
        val hasCharL = Regex("[A-Z]")

        if(hasCharU.containsMatchIn(userId)||hasCharL.containsMatchIn(userId)) {
            value=true
        }
        Constants.IsMobile=value
        return value
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}