package com.eoshopping.signpages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eoshopping.R

class Launcher_Activiry : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher_activiry)

        fun main() = runBlocking {
            val countdownTimeInSeconds = 10 // Set your countdown time here

            for (i in countdownTimeInSeconds downTo 1) {
                println("Time left: $i seconds")
                delay(1000) // Wait for 1 second
            }
            println("Time's up!")
        }
    }
}