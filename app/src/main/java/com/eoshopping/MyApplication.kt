package com.eoshopping

import android.app.Application
import android.media.MediaDrm.PlaybackComponent
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        val firebaseAppCheck=FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory (
            PlayIntegrityAppCheckProviderFactory.getInstance()
                )
    }
}