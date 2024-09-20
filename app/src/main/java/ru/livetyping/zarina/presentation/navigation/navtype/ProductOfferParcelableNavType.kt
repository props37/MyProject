package ru.livetyping.zarina.presentation.navigation.navtype

import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.livetyping.zarina.presentation.model.product.ProductOfferParcelable
import ru.livetyping.zarina.presentation.navigation.base.ParcelableNavType

val NavType.Companion.ProductOfferParcelableType: ProductOfferParcelableNavType
    get() = ProductOfferParcelableNavType()

class ProductOfferParcelableNavType : ParcelableNavType<ProductOfferParcelable?>(
    isNullableAllowed = true,
    serializer = Json.serializersModule.serializer(),
)
