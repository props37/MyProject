package ru.livetyping.zarina.presentation.model.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.checkout.Customer
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber

@Serializable
@Parcelize
data class CustomerParcelable(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
) : Parcelable {
    fun toCustomer(): Customer {
        return Customer(
            firstName = firstName,
            lastName = lastName,
            phone = PhoneNumber.create(phone),
            email = Email.create(email),
        )
    }

    companion object {
        fun from(customer: Customer): CustomerParcelable {
            return CustomerParcelable(
                firstName = customer.firstName,
                lastName = customer.lastName,
                phone = customer.phone.value,
                email = customer.email.value,
            )
        }
    }
}
