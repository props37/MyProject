package ru.livetyping.zarina.presentation.notification

import android.content.Context
import androidx.annotation.StringRes
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import ru.livetyping.zarina.R

sealed class ZarinaNotificationChannel(
    val id: String,
    val importance: Int,
    @StringRes
    val nameResId: Int,
    @StringRes
    val descriptionResId: Int,
    val showBadge: Boolean,
) {
    fun toNotificationChannel(context: Context): NotificationChannelCompat {
        return NotificationChannelCompat.Builder(id, importance)
            .setName(context.getString(nameResId))
            .setDescription(context.getString(descriptionResId))
            .setShowBadge(showBadge)
            .build()
    }

    data object Marketing : ZarinaNotificationChannel(
        id = NotificationChannelId.MARKETING.value,
        importance = NotificationManagerCompat.IMPORTANCE_DEFAULT,
        nameResId = R.string.notification_channel_marketing_name,
        descriptionResId = R.string.notification_channel_marketing_description,
        showBadge = true,
    )
}

private enum class NotificationChannelId {
    MARKETING;

    val value: String get() = name.lowercase()
}
