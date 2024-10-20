package com.eoshopping.signpages

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.eoshopping.MainActivity
import com.eoshopping.R
import com.eoshopping.common_utils.CommonUtil
import com.eoshopping.pojo.UserDo
import com.eoshopping.repository.UserRepository
import com.eoshopping.viewModel.UserViewModel
import com.eoshopping.viewModelFactory.UserViewModelFactory
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import java.io.File
import java.io.FileOutputStream

class SignUpActivity : AppCompatActivity() {
    private val REQUEST_CODE_CAMERA = 100
    private val REQUEST_CODE_GALLERY = 101
    private lateinit var imgProfile : ShapeableImageView
    private lateinit var et_fullName : TextInputEditText
    private lateinit var et_phone : TextInputEditText
    private lateinit var et_email : TextInputEditText
    private lateinit var et_password : TextInputEditText
    private lateinit var et_ConfirmPassword : TextInputEditText
    private lateinit var btn_signUp : Button
    private lateinit var tv_signIn : TextView
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false
    private  var phone :String?=""
    private  var password :String?=""
    private  var confirmPassword :String?=""
    private  var fullName :String?=""
    private  var email :String?=""
    private  var img_URL :String?=""
    private lateinit var userViewModel: UserViewModel
    private var profilePicUri: Uri?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        updateXML()
    }
    @SuppressLint("ClickableViewAccessibility")
    fun updateXML(){
        imgProfile = findViewById<ShapeableImageView>(R.id.img_profile)
        et_fullName = findViewById(R.id.et_fullName)
        et_phone = findViewById(R.id.et_phone)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        et_ConfirmPassword = findViewById(R.id.et_ConfirmPassword)
        btn_signUp= findViewById<Button>(R.id.btn_signUp)
        tv_signIn =findViewById<TextView>(R.id.tv_signIn)

        val repository=UserRepository()
        userViewModel = ViewModelProvider(this, UserViewModelFactory(repository)).get(UserViewModel::class.java)

        userViewModel.loginStatus.observe(this){status->
            if (status.first){
                Toast.makeText(this, "Register Successful", Toast.LENGTH_LONG).show()
                startActivity(Intent(this,SignInActivity::class.java))
                finish()
            }else{
                CommonUtil.showAlertDialog(this,"Alert","Register Failed")

            }
        }
        btn_signUp.setOnClickListener(View.OnClickListener {
            var alert:String?=getAlerts()
            if (alert.isNullOrEmpty()){
                val user=UserDo(fullName=fullName!!, mobileNumber = phone!!,
                    email = email!!,password=(password!!),profileImageUrl =null)
                userViewModel.registerUser(user,profilePicUri)


            }else{
                CommonUtil.showAlertDialog(this,"Alert",alert)
            }

        })
        tv_signIn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))

        })
        et_password.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // Drawable end index
                if (event.rawX >= et_password.right - et_password.compoundDrawables[drawableEnd].bounds.width()) {
                    isPasswordVisible=  CommonUtil.togglePasswordVisibility(isPasswordVisible,et_password)
                    return@setOnTouchListener true
                }
            }
            false
        }
        et_ConfirmPassword.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // Drawable end index
                if (event.rawX >= et_ConfirmPassword.right - et_ConfirmPassword.compoundDrawables[drawableEnd].bounds.width()) {
                    isConfirmPasswordVisible= CommonUtil.togglePasswordVisibility(isConfirmPasswordVisible,et_ConfirmPassword)
                    return@setOnTouchListener true
                }
            }
            false
        }
        imgProfile.setOnClickListener ( View.OnClickListener {
            selectImageSource();
        })
    }

    fun getAlerts(): String? {
        var alert: String? = ""
        fullName = et_fullName.text.toString()
        phone=et_phone.text.toString()
        email=et_email.text.toString()
        password=et_password.text.toString()
        confirmPassword=et_ConfirmPassword.text.toString()
        if (fullName.isNullOrEmpty()) {
            alert += "Please enter your full name\n"
        }
        if (phone.isNullOrEmpty()) {
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
            alert+= phone?.let { CommonUtil.isMobileValid(it,"") }
            alert+= email?.let { CommonUtil.isValidEmail(it) }
            alert+= password?.let { CommonUtil.isPasswordValid(it,"") }
            alert+= confirmPassword?.let { CommonUtil.isPasswordValid(it,"Confirm ") }
        }


        return alert
    }

    private fun selectImageSource() {
        val options = arrayOf("Camera", "Gallery")
        AlertDialog.Builder(this)
            .setTitle("Select Image Source")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }
    private fun saveBitmapToFile(bitmap: Bitmap): Uri? {
        val file = File(cacheDir, "profile_image_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        return Uri.fromFile(file)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_CAMERA -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    // Use the bitmap as needed (e.g., set to ImageView)
                    // Optionally, save the bitmap to a file for uploading
                    profilePicUri = saveBitmapToFile(imageBitmap)
                }
                REQUEST_CODE_GALLERY -> {
                    profilePicUri = data?.data
                   // profilePicUri = data?.data
                    // Use the URI as needed (e.g., set to ImageView)
                }
            }
            if (profilePicUri !=null){
                imgProfile.setImageURI(profilePicUri)

            }
        }
    }
}