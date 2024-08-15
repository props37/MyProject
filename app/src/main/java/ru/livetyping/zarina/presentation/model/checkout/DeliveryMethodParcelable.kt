package ru.livetyping.zarina.presentation.model.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.domain.checkout.DeliveryMethod
import ru.livetyping.zarina.presentation.model.order.DeliveryMethodTypeParcelable

@Parcelize
data class DeliveryMethodParcelable(
    val id: Int,
    val type: DeliveryMethodTypeParcelable,
    val name: String,
    val description: String?,
) : Parcelable {

    fun toDeliveryMethod(): DeliveryMethod {
        return DeliveryMethod(
            id = DeliveryMethod.Id(id),
            type = type.toDeliveryMethodType(),
            name = name,
            description = description,
        )
    }

    companion object {
        fun from(method: DeliveryMethod): DeliveryMethodParcelable {
            return DeliveryMethodParcelable(
                id = method.id.value,
                type = DeliveryMethodTypeParcelable.from(method.type),
                name = method.name,
                description = method.description,
            )
        }
    }
}
