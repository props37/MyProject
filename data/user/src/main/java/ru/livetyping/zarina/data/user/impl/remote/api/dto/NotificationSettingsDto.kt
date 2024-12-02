package ru.livetyping.zarina.data.user.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class NotificationSettingsDto(
    @SerialName("id")
    val name: String,

    @SerialName("is_on")
    val isEnabled: Boolean,
) {
    companion object {
        const val NAME_RECEIVE_SMS = "sms"
        const val NAME_RECEIVE_EMAILS = "email"
    }
}
