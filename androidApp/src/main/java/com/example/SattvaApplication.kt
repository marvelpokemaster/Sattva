package com.example

import android.app.Application
import com.example.data.remote.firebase.FirebaseInitializer

class SattvaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Single centralized startup initialization for Firebase infrastructure
        FirebaseInitializer.initialize(this)
    }
}
