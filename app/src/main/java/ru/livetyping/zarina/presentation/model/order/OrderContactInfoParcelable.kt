package ru.livetyping.zarina.presentation.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.order.OrderContactInfo

@Serializable
@Parcelize
data class OrderContactInfoParcelable(
    val firstName: String,
    val lastName: String?,
    val email: String,
    val phone: String?,
) : Parcelable {
    fun toOrderContactInfo(): OrderContactInfo {
        return OrderContactInfo(
            firstName = firstName,
            lastName = lastName,
            email = Email.create(email),
            phone = phone?.let { PhoneNumber.create(it) },
        )
    }

    companion object {
        fun from(info: OrderContactInfo): OrderContactInfoParcelable {
            return OrderContactInfoParcelable(
                firstName = info.firstName,
                lastName = info.lastName,
                email = info.email.value,
                phone = info.phone?.value,
            )
        }
    }
}
