package com.eoshopping

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eoshopping.Activitys.AddAddress
import com.eoshopping.Adapters.AddressAdapter
import com.eoshopping.common_utils.Constants
import com.eoshopping.pojo.UserDo
import com.eoshopping.repository.UserRepository
import com.eoshopping.viewModel.UserViewModel
import com.eoshopping.viewModelFactory.UserViewModelFactory
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileOutputStream


class AccountFragment : Fragment() {
    private val REQUEST_CODE_CAMERA = 100
    private val REQUEST_CODE_GALLERY = 101
    private lateinit var viewModel: UserViewModel
    private lateinit var viewAddModel: UserViewModel
    private lateinit var imgProfile: ShapeableImageView
    private lateinit var tv_name: TextInputEditText
    private lateinit var tv_phone: TextInputEditText
    private lateinit var tv_email: TextInputEditText
    private lateinit var imv_setting:ImageView
    private lateinit var bnt_addAddress:Button
    private lateinit var rcView_address:RecyclerView
    private lateinit var bnt_Update_pic:ImageButton
    private var profilePicUri: Uri?=null
    private lateinit var addressAdapter: AddressAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_account, container, false)
        updateXML(view)
        getUserData(Constants.USER_ID)
        showAddress()
        return view
    }
   private fun updateXML(view: View){
       imgProfile = view.findViewById(R.id.img_profile)
        tv_name = view.findViewById(R.id.et_Name)
        tv_email = view.findViewById(R.id.et_email)
        tv_phone = view.findViewById(R.id.et_mobileNumber)
        imv_setting =view.findViewById(R.id.imv_setting)
        bnt_addAddress =view.findViewById(R.id.bnt_addAddress)
        rcView_address =view.findViewById(R.id.rcView_address)
       bnt_Update_pic= view.findViewById(R.id.bnt_Update_pic)
       bnt_Update_pic.setOnClickListener ( View.OnClickListener {
           selectImageSource();
       })
       bnt_addAddress.setOnClickListener(View.OnClickListener {
           val intent = Intent(activity, AddAddress::class.java)
           intent.putExtra("UEmail", tv_email.text.toString())
           intent.putExtra("UMobileNumber", tv_phone.text.toString())
           startActivity(intent)
       })
    }
   private fun getUserData(userId:String){
       val repository= UserRepository()
       viewModel = ViewModelProvider(this, UserViewModelFactory(repository)).get(UserViewModel::class.java)

       viewModel.getUserData(userId)
       viewModel.userStatus.observe(viewLifecycleOwner){user->
           user?.let {
               displayUserData(it)
           }
       }
   }
    private fun displayUserData(userDo: UserDo){
        tv_name.setText(userDo.fullName)
        tv_phone.setText(userDo.mobileNumber)
        tv_email.setText(userDo.email)

        userDo.profileImageUrl?.let {
            url->
            Glide.with(requireActivity()).load(url)
                .into(imgProfile)

        }

    }
    private fun selectImageSource() {
        val options = arrayOf("Camera", "Gallery")
        AlertDialog.Builder(requireContext())
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
        val file = File(requireActivity().cacheDir, "profile_image_${System.currentTimeMillis()}.jpg")
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
                    profilePicUri = saveBitmapToFile(imageBitmap)
                }
                REQUEST_CODE_GALLERY -> {
                    profilePicUri = data?.data

                }
            }
            if (profilePicUri !=null){
                imgProfile.setImageURI(profilePicUri)

            }
        }
    }

    private fun showAddress(){
        rcView_address.layoutManager =LinearLayoutManager(requireContext())
        addressAdapter = AddressAdapter(emptyList())
        val repository= UserRepository()
        viewAddModel = ViewModelProvider(this, UserViewModelFactory(repository)).get(UserViewModel::class.java)

        rcView_address.adapter = addressAdapter
        viewAddModel.getAddress(Constants.USER_ID,Constants.USER_ID)

        viewAddModel.addressList.observe(viewLifecycleOwner) { list ->
            addressAdapter.updateItems(list)
            bnt_addAddress.isEnabled = list.size < 3
        }
    }

    override fun onStart() {
        super.onStart()
    }
}
