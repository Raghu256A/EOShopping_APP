package com.eoshopping.signpages

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.eoshopping.R
import com.eoshopping.common_utils.CommonUtil
import com.google.android.material.textfield.TextInputEditText

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var et_UserId: TextInputEditText
    private lateinit var et_password: TextInputEditText
    private lateinit var et_ConfirmPassword: TextInputEditText
    private lateinit var tv_signIn: TextView
    private lateinit var tv_signUp: TextView
    private lateinit var btn_changePassword: Button

    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false
    private var userId: String? = ""
    private var password: String? = ""
    private var confirmPassword: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        updateXML()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun updateXML() {

        et_UserId = findViewById(R.id.et_UserId)
        et_password = findViewById(R.id.et_password)
        et_ConfirmPassword = findViewById(R.id.et_ConfirmPassword)
        tv_signIn = findViewById<TextView>(R.id.tv_signIn)
        tv_signUp = findViewById<TextView>(R.id.tv_signUp)
        btn_changePassword = findViewById<Button>(R.id.btn_changePassword)
        btn_changePassword.setOnClickListener(View.OnClickListener {

        })

        tv_signIn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))

        })
        tv_signUp.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))

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
        et_ConfirmPassword.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // Drawable end index
                if (event.rawX >= et_ConfirmPassword.right - et_ConfirmPassword.compoundDrawables[drawableEnd].bounds.width()) {
                    isConfirmPasswordVisible = CommonUtil.togglePasswordVisibility(
                        isConfirmPasswordVisible,
                        et_ConfirmPassword
                    )
                    return@setOnTouchListener true
                }
            }
            false
        }

    }
    fun getAlerts(): String? {
        var alert: String? = ""

        userId=et_UserId.text.toString()
        password=et_password.text.toString()
        confirmPassword=et_ConfirmPassword.text.toString()

        if (userId.isNullOrEmpty()) {
            alert += "Please enter phone\n"
        }
        if (password.isNullOrEmpty()) {
            alert += "Please enter password\n"
        }
        if (confirmPassword.isNullOrEmpty()) {
            alert += "Please enter confirm password\n"

        }
        if (alert.isNullOrEmpty()){
            if (!confirmPassword.isNullOrEmpty()&&!confirmPassword.equals(password)) {
                alert += "Confirm password and password should not be match\n"

            }
            val hasCharU = Regex("[a-z]")
            val hasCharL = Regex("[A-Z]")

            if(hasCharU.containsMatchIn(userId!!)||hasCharL.containsMatchIn(userId!!)) {
                alert += userId?.let { CommonUtil.isValidEmail(it) }
            } else {
                alert += userId?.let { CommonUtil.isMobileValid(it, "") }

            }
            alert+= password?.let { CommonUtil.isPasswordValid(it,"") }
            alert+= confirmPassword?.let { CommonUtil.isPasswordValid(it,"Confirm ") }
        }


        return alert
    }
}