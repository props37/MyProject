package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.navigationutil.ParcelableNavType
import ru.livetyping.zarina.core.uimodel.product.ProductShortParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
internal class AvailabilityInStoresNavEntry(
    val product: ProductShortParcelable,
) {
    companion object {
        fun from(product: Product): AvailabilityInStoresNavEntry {
            val productParcelable = ProductShortParcelable.from(product)
            return AvailabilityInStoresNavEntry(productParcelable)
        }

        fun typeMap(): Map<KType, NavType<*>> {
            val productNavType = ParcelableNavType<ProductShortParcelable>(
                isNullableAllowed = false,
                serializer = kotlinx.serialization.serializer(),
            )
            return mapOf(typeOf<ProductShortParcelable>() to productNavType)
        }
    }
}
