package com.eoshopping.common_utils

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.eoshopping.R
import com.eoshopping.common_utils.Constants.Companion.ALGORITHM
import com.eoshopping.common_utils.Constants.Companion.KEY
import com.google.android.material.textfield.TextInputEditText
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class CommonUtil {


    companion object {

        fun showAlertDialog(context: Context, title: String, msg: String) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss() // Close the dialog
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss() // Close the dialog
                }

            // Create and show the AlertDialog
            val alertDialog = builder.create()
            alertDialog.show()

        }

        fun togglePasswordVisibility(
            isPasswordVisible: Boolean,
            et_password: TextInputEditText
        ): Boolean {
            if (isPasswordVisible) {
                // Hide password
                et_password.transformationMethod = PasswordTransformationMethod.getInstance()
                et_password.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.password_invisable,
                    0
                ) // Change drawable to eye off
            } else {
                // Show password
                et_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                et_password.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.password_visibility,
                    0
                ) // Change drawable to eye on
            }
            et_password.text?.let { et_password.setSelection(it.length) } // Move cursor to end
            return !isPasswordVisible
        }

        fun isMobileValid(mobileNo: String, type: String): String? {
            var alert: String? = ""

            if (mobileNo.length != 10) {
                alert += "Please enter 10 digits $type mobile number \n"
            } else if (!mobileNo.startsWith("9") && !mobileNo.startsWith("8")
                && !mobileNo.startsWith("7") && !mobileNo.startsWith("6")
            ) {
                alert += "Please enter valid $type mobile number \n"
            }

            return alert
        }

        fun isPasswordValid(password: String, type: String): String? {
            var alert: String? = ""
            val hasUpperCase = Regex("[A-Z]")
            val hasLowerCase = Regex("[a-z]")
            val hasDigit = Regex("[0-9]")
            val hasSpecialChar = Regex("[!@#$%^&*?]")
            if (password.length < 6) {
                alert += type + "Password should above 5 characters \n"
            } else if (!hasUpperCase.containsMatchIn(password)) {
                alert += type + "Password should min one upper latter required \n"
            } else if (!hasUpperCase.containsMatchIn(password)) {
                alert += type + "Password should min one upper latter required \n"
            } else if (!hasLowerCase.containsMatchIn(password)) {
                alert += type + "Password should min one lower latter required \n"
            } else if (!hasDigit.containsMatchIn(password)) {
                alert += type + "Password should min one digit required \n"
            } else if (!hasSpecialChar.containsMatchIn(password)) {
                alert += type + "Password should min one specialChar required like !@#\$%^&*? \n"
            }

            return alert
        }

        fun isValidEmail(email: String): String {
            val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
            if (!emailRegex.matches(email)) {
                return "Invalid email address.\n"
            }
            return ""
        }

        fun openDialog(context: Context): ProgressDialog {
            var dialog: ProgressDialog? = null
            dialog = ProgressDialog(context).apply {
                setMessage("Please wait...")
                setCancelable(false)
                show()
            }
            val customDrawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.progress_icon)
            val progressBar = dialog.findViewById<ProgressBar>(android.R.id.progress)
            progressBar?.indeterminateDrawable = customDrawable

            return dialog
        }

        fun closeDialog(dialog: ProgressDialog) {
            if (dialog != null && dialog.isShowing) {
                dialog.dismiss()
            }

        }

    }

}