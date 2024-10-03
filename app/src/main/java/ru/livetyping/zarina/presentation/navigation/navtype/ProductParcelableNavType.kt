package ru.livetyping.zarina.presentation.navigation.navtype

import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.livetyping.zarina.presentation.model.product.ProductItemParcelable
import ru.livetyping.zarina.presentation.navigation.base.ParcelableNavType

val NavType.Companion.ProductParcelableType: ProductParcelableNavType
    get() = ProductParcelableNavType()

class ProductParcelableNavType : ParcelableNavType<ProductItemParcelable?>(
    isNullableAllowed = true,
    serializer = Json.serializersModule.serializer(),
)
