package ru.livetyping.zarina.data.mindbox.impl.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.user.User

@Serializable
internal class UserDto private constructor(
    @SerialName("customer")
    private val customer: CustomerDto,
) {
    @Serializable
    private class CustomerDto(
        @SerialName("ids")
        val ids: IdsDto,
    ) {
        @Serializable
        class IdsDto(
            @SerialName("siteid")
            val id: String,
        )
    }

    companion object {
        fun from(user: User): UserDto {
            val customer = CustomerDto(CustomerDto.IdsDto(user.id.value))
            return UserDto(customer)
        }
    }
}
