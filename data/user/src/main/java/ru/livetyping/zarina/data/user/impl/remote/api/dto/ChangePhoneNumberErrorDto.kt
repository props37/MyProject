package ru.livetyping.zarina.data.user.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.user.exception.InvalidPhoneNumberException

@Serializable
internal data class ChangePhoneNumberErrorDto(
    @SerialName("field_name")
    val fieldName: String? = null,

    @SerialName("description")
    val description: String? = null,
) {
    fun toException(): Exception {
        checkNotNull(fieldName) { "fieldName is null" }
        return when (fieldName) {
            "phone" -> InvalidPhoneNumberException()
            else -> error("Unknown field name $fieldName")
        }
    }
}
