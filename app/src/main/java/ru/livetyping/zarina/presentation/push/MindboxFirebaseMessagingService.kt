package ru.livetyping.zarina.presentation.push

import cloud.mindbox.mindbox_firebase.MindboxFirebase
import cloud.mindbox.mobile_sdk.Mindbox
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class MindboxFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Mindbox.updatePushToken(this, token, MindboxFirebase)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Timber.tag(TAG).v("onMessageReceived: ${message.data.toMap()}")
        if (MindboxFirebase.isMindboxPush(message)) {
            val mindboxMessage = MindboxFirebase.convertToMindboxRemoteMessage(message)
            if (mindboxMessage == null) {
                Timber.tag(MINDBOX_TAG).e("Failed to convert RemoteMessage $message to MindboxRemoteMessage")
                return
            }
            Mindbox.onPushReceived(this, mindboxMessage.uniqueKey)
            // TODO: [High] Implement
        }
    }

    companion object {
        private const val TAG = "MindboxFirebaseMessagingService"
        private const val MINDBOX_TAG = "Mindbox"
    }
}
