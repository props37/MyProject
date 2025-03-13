package ru.livetyping.zarina.presentation.notification.push

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.core.net.toUri
import cloud.mindbox.mindbox_firebase.MindboxFirebase
import cloud.mindbox.mobile_sdk.Mindbox
import cloud.mindbox.mobile_sdk.pushes.MindboxRemoteMessage
import coil.imageLoader
import coil.request.ImageRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.activity.MainActivity
import ru.livetyping.zarina.presentation.notification.ZarinaNotificationChannel
import ru.livetyping.zarina.util.platform.isPermissionGranted
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MindboxFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var coroutineScope: CoroutineScope

    private val notificationManager by lazy {
        NotificationManagerCompat.from(this)
    }

    override fun onNewToken(token: String) {
        Timber.tag(TAG).v("onNewToken: $token")
        Mindbox.updatePushToken(this, token, MindboxFirebase)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Timber.tag(TAG).v("onMessageReceived: ${message.data.toMap()}")
        if (!isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)) return

        if (MindboxFirebase.isMindboxPush(message)) {
            val mindboxMessage = MindboxFirebase.convertToMindboxRemoteMessage(message)
            if (mindboxMessage == null) {
                Timber.tag(TAG).e("Failed to convert RemoteMessage $message to MindboxRemoteMessage")
                return
            }
            Timber.tag(TAG).v("Mindbox message: $mindboxMessage")
            Mindbox.onPushReceived(this, mindboxMessage.uniqueKey)
            coroutineScope.launch {
                handleMindboxMessage(mindboxMessage)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun handleMindboxMessage(message: MindboxRemoteMessage) {
        val notificationChannel = getNotificationChannel()
        notificationManager.createNotificationChannel(notificationChannel)

        val notification = getNotification(message, notificationChannel)
        val notificationId = message.uniqueKey.hashCode()
        if (isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)) {
            notificationManager.notify(notificationId, notification)
        }
    }

    private fun getNotificationChannel(): NotificationChannelCompat {
        val zarinaNotificationChannel = ZarinaNotificationChannel.Marketing
        return zarinaNotificationChannel.toNotificationChannel(this)
    }

    private suspend fun getNotification(
        message: MindboxRemoteMessage,
        channel: NotificationChannelCompat,
    ): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, channel.id)
        return notificationBuilder.apply {
            setSmallIcon(R.drawable.ic_notification)
            setContentTitle(message.title)
            setContentText(message.description)
            setStyle(getNotificationStyle(message))
            val pendingIntent = message.pushLink?.let {
                getUrlPendingIntent(
                    actionUrl = it,
                    uniquePushKey = message.uniqueKey,
                    uniquePushButtonKey = null,
                )
            } ?: getMainActivityPendingIntent()
            setContentIntent(pendingIntent)
            setAutoCancel(true)
            val actions = getActions(message)
            actions.forEach { action ->
                addAction(action)
            }
        }.build()
    }

    private suspend fun getNotificationStyle(
        message: MindboxRemoteMessage,
    ): NotificationCompat.Style {
        val imageUrl = message.imageUrl
        val imageBitmap = imageUrl?.let { getBitmap(it) }
        return when {
            imageBitmap != null -> {
                val bigPicture = NotificationCompat.BigPictureStyle()
                    .bigPicture(imageBitmap)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    bigPicture.showBigPictureWhenCollapsed(true)
                }
                bigPicture
            }

            else -> {
                NotificationCompat.BigTextStyle()
                    .bigText(message.description)
            }
        }
    }

    private fun getActions(message: MindboxRemoteMessage): List<NotificationCompat.Action> {
        return message.pushActions.mapNotNull { action ->
            if (action.text != null && action.url != null) {
                val pendingIntent = action.url?.let {
                    getUrlPendingIntent(
                        actionUrl = it,
                        uniquePushKey = message.uniqueKey,
                        uniquePushButtonKey = action.uniqueKey,
                    )
                } ?: getMainActivityPendingIntent()
                NotificationCompat.Action.Builder(null, action.text, pendingIntent)
                    .build()
            } else {
                null
            }
        }
    }

    private fun getUrlPendingIntent(
        actionUrl: String,
        uniquePushKey: String?,
        uniquePushButtonKey: String?,
    ): PendingIntent? {
        val intent = Intent(
            /* action = */ Intent.ACTION_VIEW,
            /* uri = */ actionUrl.toUri(),
            /* packageContext = */ this,
            /* cls = */ MainActivity::class.java,
        )
        if (uniquePushKey != null) {
            intent.putExtra(MINDBOX_EXTRA_UNIQ_PUSH_KEY, uniquePushKey)
        }
        if (uniquePushButtonKey != null) {
            intent.putExtra(MINDBOX_EXTRA_UNIQ_PUSH_BUTTON_KEY, uniquePushButtonKey)
        }

        val taskBuilder = TaskStackBuilder.create(this).apply {
            addNextIntentWithParentStack(intent)
        }
        return taskBuilder.getPendingIntent(
            /* requestCode = */ 0,
            /* flags = */ PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    private fun getMainActivityPendingIntent(): PendingIntent {
        return PendingIntent.getActivity(
            /* context = */ this,
            /* requestCode = */ 0,
            /* intent = */ Intent(this, MainActivity::class.java),
            /* flags = */ PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    private suspend fun getBitmap(imageUrl: String): Bitmap? {
        val imageLoader = this.imageLoader
        val request = ImageRequest.Builder(this)
            .data(imageUrl)
            .allowHardware(false)
            .build()
        return withContext(Dispatchers.IO) {
            val result = imageLoader.execute(request)
            result.drawable?.toBitmapOrNull()
        }
    }

    companion object {
        // Mindbox extra keys used to track push clicks
        // Copied from cloud.mindbox.mobile_sdk.pushes.PushNotificationManager since they are private
        private const val MINDBOX_EXTRA_UNIQ_PUSH_KEY = "uniq_push_key"
        private const val MINDBOX_EXTRA_UNIQ_PUSH_BUTTON_KEY = "uniq_push_button_key"

        private const val TAG = "MindboxFirebaseMessagingService"
    }
}
