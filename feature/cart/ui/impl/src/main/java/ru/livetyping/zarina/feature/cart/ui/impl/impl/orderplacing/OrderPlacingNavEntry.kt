package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.CheckoutParams
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.parcelableNavType
import ru.livetyping.zarina.core.uimodel.checkout.CheckoutParamsParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
internal class OrderPlacingNavEntry private constructor(
    val checkoutStep: Int,
    val checkoutParams: CheckoutParamsParcelable,
) : NavigationEntry {
    companion object {
        fun from(checkoutStep: Int, checkoutParams: CheckoutParams): OrderPlacingNavEntry {
            return OrderPlacingNavEntry(
                checkoutStep = checkoutStep,
                checkoutParams = CheckoutParamsParcelable.from(checkoutParams),
            )
        }

        fun typeMap(): Map<KType, NavType<*>> {
            val checkoutParamsType =
                parcelableNavType<CheckoutParamsParcelable>(isNullableAllowed = false)
            return mapOf(typeOf<CheckoutParamsParcelable>() to checkoutParamsType)
        }
    }
}
