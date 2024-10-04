package com.eoshopping.signpages

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.eoshopping.MainActivity
import com.eoshopping.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LaunchActivity : AppCompatActivity() {
    private var job: Job?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        changeScreen(10)


    }
    private fun changeScreen(time :Int ){
        job= CoroutineScope(Dispatchers.Main).launch {
            for(i in time downTo 0){
                delay(1000)
            }
            val intent = Intent(this@LaunchActivity, SignInActivity::class.java)
            startActivity(intent)
        }

    }


}