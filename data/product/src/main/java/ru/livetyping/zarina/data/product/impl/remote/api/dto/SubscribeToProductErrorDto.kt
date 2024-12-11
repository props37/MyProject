package ru.livetyping.zarina.data.product.impl.remote.api.dto

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
internal sealed class SubscribeToProductErrorDto

@Serializable
internal data class SubscribeToProductEmailErrorDto(
    @SerialName("message")
    val message: Message? = null,
) : SubscribeToProductErrorDto() {

    @Serializable
    data class Message(
        @SerialName("email")
        val email: List<String>? = null,
    )
}

@Serializable
internal data class SubscribeToProductFirstNameErrorDto(
    @SerialName("message")
    val message: Message? = null,
) : SubscribeToProductErrorDto() {

    @Serializable
    data class Message(
        @SerialName("first_name")
        val firstName: List<String>? = null,
    )
}

internal class SubscribeToProductErrorDtoSerializer :
    JsonContentPolymorphicSerializer<SubscribeToProductErrorDto>(SubscribeToProductErrorDto::class) {

    override fun selectDeserializer(
        element: JsonElement,
    ): DeserializationStrategy<SubscribeToProductErrorDto> {
        val message = element.jsonObject["message"] ?: error(UNKNOWN_ERROR)
        return when {
            "email" in message.jsonObject -> SubscribeToProductEmailErrorDto.serializer()
            "first_name" in message.jsonObject -> SubscribeToProductFirstNameErrorDto.serializer()
            else -> error(UNKNOWN_ERROR)
        }
    }

    companion object {
        private const val UNKNOWN_ERROR = "Unknown SubscribeToProductEmail error"
    }
}
