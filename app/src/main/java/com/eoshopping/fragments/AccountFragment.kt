package com.eoshopping

import android.os.Bundle
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eoshopping.common_utils.Constants
import com.eoshopping.pojo.UserDo
import com.eoshopping.repository.UserRepository
import com.eoshopping.viewModel.UserViewModel
import com.eoshopping.viewModelFactory.UserViewModelFactory
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText


class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: UserViewModel
    private lateinit var imageView: ShapeableImageView
    private lateinit var tv_name: TextInputEditText
    private lateinit var tv_phone: TextInputEditText
    private lateinit var tv_email: TextInputEditText
    private lateinit var imv_setting:ImageView
    private lateinit var bnt_addAddress:Button
    private lateinit var rcView_address:RecyclerView
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
        return view
    }
   private fun updateXML(view: View){
        imageView = view.findViewById(R.id.img_profile)
        tv_name = view.findViewById(R.id.et_Name)
        tv_email = view.findViewById(R.id.et_email)
        tv_phone = view.findViewById(R.id.et_mobileNumber)
        imv_setting =view.findViewById(R.id.imv_setting)
        bnt_addAddress =view.findViewById(R.id.bnt_addAddress)
        rcView_address =view.findViewById(R.id.rcView_address)
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
                .into(imageView)

        }

    }

}
