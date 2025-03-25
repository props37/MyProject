package ru.livetyping.zarina.core.uimodel.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod

@Parcelize
@Serializable
public data class DeliveryMethodParcelable(
    val id: Int,
    val type: DeliveryMethodTypeParcelable,
    val name: String,
    val description: String?,
) : Parcelable {
    public fun toDeliveryMethod(): DeliveryMethod {
        return DeliveryMethod(
            id = DeliveryMethod.Id(id),
            type = type.toDeliveryMethodType(),
            name = name,
            description = description,
        )
    }

    public companion object {
        public fun from(deliveryMethod: DeliveryMethod): DeliveryMethodParcelable {
            return DeliveryMethodParcelable(
                id = deliveryMethod.id.value,
                type = DeliveryMethodTypeParcelable.from(deliveryMethod.type),
                name = deliveryMethod.name,
                description = deliveryMethod.description,
            )
        }
    }
}
