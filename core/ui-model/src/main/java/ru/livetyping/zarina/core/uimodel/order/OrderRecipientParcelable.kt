package ru.livetyping.zarina.core.uimodel.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.order.OrderRecipient

@Serializable
@Parcelize
public data class OrderRecipientParcelable(
    val firstName: String,
    val lastName: String?,
    val email: String,
    val phone: String?,
) : Parcelable {
    public fun toOrderRecipient(): OrderRecipient {
        return OrderRecipient(
            firstName = firstName,
            lastName = lastName,
            email = Email.create(email),
            phone = phone?.let { PhoneNumber.create(it) },
        )
    }

    public companion object {
        public fun from(info: OrderRecipient): OrderRecipientParcelable {
            return OrderRecipientParcelable(
                firstName = info.firstName,
                lastName = info.lastName,
                email = info.email.value,
                phone = info.phone?.value,
            )
        }
    }
}
