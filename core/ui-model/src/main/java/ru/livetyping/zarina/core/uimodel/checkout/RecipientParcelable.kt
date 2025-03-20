package ru.livetyping.zarina.core.uimodel.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

@Serializable
@Parcelize
public data class RecipientParcelable(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
) : Parcelable {
    public fun toRecipient(): Recipient {
        return Recipient(
            firstName = firstName,
            lastName = lastName,
            phone = PhoneNumber.create(phone),
            email = Email.create(email),
        )
    }

    public companion object {
        public fun from(recipient: Recipient): RecipientParcelable {
            return RecipientParcelable(
                firstName = recipient.firstName,
                lastName = recipient.lastName,
                phone = recipient.phone.value,
                email = recipient.email.value,
            )
        }
    }
}
