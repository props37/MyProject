package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.domain.user.exception.InvalidEmailException
import ru.livetyping.zarina.domain.user.exception.InvalidFirstNameException
import ru.livetyping.zarina.domain.user.exception.InvalidPasswordException
import ru.livetyping.zarina.domain.user.exception.InvalidPhoneNumberException

@Serializable
sealed class SignUpErrorDto

@Serializable
data class SignUpFieldValidationErrorDto(
    @SerialName("field_name") 
    val fieldName: String? = null,
    
    @SerialName("description")
    val description: String? = null,
) : SignUpErrorDto() {
    fun toValidationException(): ValidationException {
        checkNotNull(fieldName) { "fieldName is null" }
        return when (fieldName) {
            "first_name" -> InvalidFirstNameException()
            "email" -> InvalidEmailException()
            "phone" -> InvalidPhoneNumberException()
            "password" -> InvalidPasswordException()
            else -> error("Unknown field name $fieldName")
        }
    }
}

@Serializable
data class SignUpMessageErrorDto(
    @SerialName("message")
    val message: String? = null,
) : SignUpErrorDto()

class SignUpErrorDtoSerializer :
    JsonContentPolymorphicSerializer<SignUpErrorDto>(SignUpErrorDto::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<SignUpErrorDto> {
        return when {
            "message" in element.jsonObject -> SignUpMessageErrorDto.serializer()
            else -> error(UNKNOWN_ERROR)
        }
    }

    companion object {
        private const val UNKNOWN_ERROR = "Unknown SignUp error"
    }
}
