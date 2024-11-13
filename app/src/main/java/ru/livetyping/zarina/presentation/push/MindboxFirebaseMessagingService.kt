package ru.livetyping.zarina.presentation.push

import cloud.mindbox.mindbox_firebase.MindboxFirebase
import cloud.mindbox.mobile_sdk.Mindbox
import com.google.firebase.messaging.FirebaseMessagingService

class MindboxFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Mindbox.updatePushToken(this, token, MindboxFirebase)
    }
}