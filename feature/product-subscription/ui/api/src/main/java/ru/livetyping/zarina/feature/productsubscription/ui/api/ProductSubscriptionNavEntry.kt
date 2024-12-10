package ru.livetyping.zarina.feature.productsubscription.ui.api

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.ParcelableNavType
import ru.livetyping.zarina.core.uimodel.product.ProductOfferParcelable
import ru.livetyping.zarina.core.uimodel.product.ProductShortParcelable
import ru.livetyping.zarina.core.uimodel.product.filter.ProductFiltersParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
public data class ProductSubscriptionNavEntry(
    val product: ProductShortParcelable,
    val offer: ProductOfferParcelable,
) : NavigationEntry {
    public companion object {
        public fun typeMap(): Map<KType, NavType<*>> {
            val productType = object : ParcelableNavType<ProductFiltersParcelable?>(
                isNullableAllowed = true,
                serializer = kotlinx.serialization.serializer(),
            ) {}
            val offerType = object : ParcelableNavType<ProductOfferParcelable?>(
                isNullableAllowed = true,
                serializer = kotlinx.serialization.serializer(),
            ) {}
            return mapOf(
                typeOf<ProductShortParcelable>() to productType,
                typeOf<ProductOfferParcelable>() to offerType,
            )
        }
    }
}
