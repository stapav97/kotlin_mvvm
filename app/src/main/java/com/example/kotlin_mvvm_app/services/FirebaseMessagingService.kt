package com.example.kotlin_mvvm_app.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseMessageReceiver : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("FIREBASE MESSAGE  = ", "received")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("FIREBASE MESSAGING TOKEN = ", "New Token")
    }
}