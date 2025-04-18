package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.parcelableNavType
import ru.livetyping.zarina.core.uimodel.order.OrderDetailedParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
internal class OrderConfirmedNavEntry private constructor(
    val order: OrderDetailedParcelable,
) : NavigationEntry {
    companion object {
        fun from(order: OrderDetailed): OrderConfirmedNavEntry {
            val orderParcelable = OrderDetailedParcelable.from(order)
            return OrderConfirmedNavEntry(orderParcelable)
        }

        fun typeMap(): Map<KType, NavType<*>> {
            val orderType = parcelableNavType<OrderDetailedParcelable>(isNullableAllowed = false)
            return mapOf(typeOf<OrderDetailedParcelable>() to orderType)
        }
    }
}
