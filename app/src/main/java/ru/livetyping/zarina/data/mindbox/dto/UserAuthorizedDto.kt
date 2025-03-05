package ru.livetyping.zarina.data.mindbox.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.user.User

@Serializable
data class UserAuthorizedDto(
    @SerialName("customer")
    val customer: Customer,
) {
    @Serializable
    data class Customer(
        @SerialName("ids")
        val ids: Ids,
    ) {
        @Serializable
        data class Ids(
            @SerialName("siteid")
            val id: String,
        )
    }

    companion object {
        fun from(user: User): UserAuthorizedDto {
            val customer = Customer(Customer.Ids(user.id.value))
            return UserAuthorizedDto(customer)
        }
    }
}
