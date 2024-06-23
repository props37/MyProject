package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.user.User

@Serializable
data class NotificationSettingsDto(
    @SerialName("id")
    val name: String,

    @SerialName("is_on")
    val isEnabled: Boolean,
) {
    companion object {
        fun from(settings: User.NotificationSettings): List<NotificationSettingsDto> {
            return listOf(
                NotificationSettingsDto(
                    name = NAME_RECEIVE_SMS,
                    isEnabled = settings.receiveSms,
                ),
                NotificationSettingsDto(
                    name = NAME_RECEIVE_EMAILS,
                    isEnabled = settings.receiveEmails,
                ),
            )
        }

        const val NAME_RECEIVE_SMS = "sms"
        const val NAME_RECEIVE_EMAILS = "email"
    }
}
