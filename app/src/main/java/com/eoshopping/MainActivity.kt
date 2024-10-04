package com.eoshopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


        var bottomNavigation = findViewById<BottomNavigationView>(R.id.btm_nav)
        var navController = Navigation.findNavController(this,R.id.host_fragment )
        NavigationUI.setupWithNavController(bottomNavigation,navController)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}