package com.eoshopping.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.eoshopping.common_utils.Constants
import com.eoshopping.pojo.AddressDo
import com.eoshopping.pojo.UserDo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerUser(user: UserDo, profilePicUri: Uri?, callback: (Boolean?, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser!!.uid

                    if (profilePicUri != null) {
                        uploadProfilePic(profilePicUri, user.fullName) { imageUri ->
                            if (imageUri != null) {
                                saveUserData(
                                    userId,
                                    user.copy(profileImageUrl = imageUri),
                                    callback
                                )
                            } else {
                                callback(false, "Profile pic upload failed try again..!")
                            }
                        }
                    } else {
                        saveUserData(userId, user.copy(profileImageUrl = null), callback)
                    }
                }
            }

    }

    fun uploadProfilePic(imageUri: Uri, userName: String, callback: (String?) -> Unit) {
        val file =
            storage.reference.child(userName + "_profilePic/${System.currentTimeMillis()}.jpg")
        file.putFile(imageUri).addOnSuccessListener {
            file.downloadUrl.addOnSuccessListener { uri ->
                callback(uri.toString())
            }.addOnFailureListener {
                callback(null)
            }
        }.addOnFailureListener {
            callback(null)
        }
    }

    fun saveUserData(userId: String?, user: UserDo, callback: (Boolean, String?) -> Unit) {
        if (userId != null) {
            db.getReference("UserDetails").child(userId).setValue(user)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful, task.exception?.message)
                }
        } else {
            callback(false, "User Id is null try again...!")
        }
    }

    fun loginWithEmail(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful, task.exception?.message)
            }
    }

    fun loginWithMobileNumber(
        mobileNo: String,
        password: String,
        callback: (Boolean, String?) -> Unit
    ) {
        db.getReference("UserDetails").orderByChild("mobileNumber").equalTo(mobileNo)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userdata in snapshot.children) {
                            val user = userdata.getValue(UserDo::class.java)
                            if (user != null && user.password == password) {
                                callback(true, "login success")
                                return
                            } else {
                                callback(false, "Incorrect Password")
                            }
                        }
                    } else {
                        callback(false, "User not found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message)
                }
            })
    }

    suspend fun getUserData(userId: String?): UserDo? {
        return try {
            if (!Constants.IsMobile) {
                val reference =
                    db.getReference("UserDetails").orderByChild("mobileNumber").equalTo(userId)
                        .get().await()
                if (reference.exists()) {
                    val userMap = reference.children.first().getValue(UserDo::class.java)
                    userMap
                } else {
                    null // User not found
                }
            } else {
                val reference =
                    db.getReference("UserDetails").orderByChild("email").equalTo(userId).get()
                        .await()
                if (reference.exists()) {
                    val userMap = reference.children.first().getValue(UserDo::class.java)
                    userMap
                } else {
                    null // User not found
                }

            }


        } catch (e: Exception) {
            null
        }

    }

    suspend fun saveUserAddress(
        userIdM: String?,
        userIdE: String?,
        add: AddressDo,
        callback: (Boolean, String?) -> Unit
    ) {
        if (userIdE != null || userIdM != null) {
            val addressDB = db.getReference("UserAddressDetails")
            var addressDBId = addressDB.push().key
            add.id = addressDBId

            if (!Constants.IsMobile) {
                val reference =
                    db.getReference("UserDetails").orderByChild("mobileNumber").equalTo(userIdM)
                        .get().await()
                if (reference.exists()) {

                    addressDBId?.let {
                        addressDB.child(add.id!!).setValue(add)
                            .addOnCompleteListener { task ->
                                callback(task.isSuccessful, task.exception?.message)
                            }.await()

                    }

                } else {
                    callback(false, "User Id is null try again...!")
                }
            } else {
                val reference =
                    db.getReference("UserDetails").orderByChild("email").equalTo(userIdE).get()
                        .await()
                if (reference.exists()) {
                    addressDBId?.let {
                        addressDB.child(add.id!!).setValue(add)
                            .addOnCompleteListener { task ->
                                callback(task.isSuccessful, task.exception?.message)
                            }.await()
                    }
                } else {
                    callback(false, "User Id is null try again...!")
                }

            }


        } else {
            callback(false, "User Id is null try again...!")
        }
    }

    suspend fun getAddress(userIdM: String?, userIdE: String?): List<AddressDo> {
        val itemList = mutableListOf<AddressDo>()
        try {
            if (!Constants.IsMobile) {
                val reference = db.getReference("UserAddressDetails").orderByChild("userMobile")
                    .equalTo(userIdM).get().await()
                if (reference.exists()) {

                    for (childSnapshot in reference.children) {
                        val item = childSnapshot.getValue(AddressDo::class.java)
                        item?.let { itemList.add(it) }
                    }
                }
            } else {
                val reference =
                    db.getReference("UserAddressDetails").orderByChild("userEmail").equalTo(userIdE)
                        .get().await()
                if (reference.exists()) {
                    for (childSnapshot in reference.children) {
                        val item = childSnapshot.getValue(AddressDo::class.java)
                        item?.let { itemList.add(it) }
                    }
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return itemList
    }

    suspend fun updatePassword(userId: String?,password: String?, isMobile: Boolean?,callback: (Boolean, String?) -> Unit
    ) {
        var userDB: Query? =null
        if (isMobile == true) {
           userDB= db.getReference("UserDetails").orderByChild("mobileNumber")

        } else {
            userDB= db.getReference("UserDetails").orderByChild("email")

        }

          userDB.equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userdata in snapshot.children) {
                            val UserId = userdata.key
                            auth.createUserWithEmailAndPassword(UserId!!, password!!).addOnCompleteListener { a ->
                                db.getReference("UserDetails").child(UserId).child("password").setValue(password)
                                    .addOnCompleteListener { task ->

                                        callback(task.isSuccessful, task.exception?.message)
                                    }
                            }


                        }
                    } else {
                        callback(false, "User not found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message)
                }
            })


    }
}