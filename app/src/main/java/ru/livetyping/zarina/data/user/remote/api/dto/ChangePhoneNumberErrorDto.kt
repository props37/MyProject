package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.domain.user.exception.InvalidPhoneNumberException

@Serializable
data class ChangePhoneNumberErrorDto(
    @SerialName("field_name")
    val fieldName: String? = null,

    @SerialName("description")
    val description: String? = null,
) {
    fun toValidationException(): ValidationException {
        checkNotNull(fieldName) { "fieldName is null" }
        return when (fieldName) {
            "phone" -> InvalidPhoneNumberException()
            else -> error("Unknown field name $fieldName")
        }
    }
}
