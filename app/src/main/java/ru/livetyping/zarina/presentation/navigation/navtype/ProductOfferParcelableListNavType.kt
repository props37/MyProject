package ru.livetyping.zarina.presentation.navigation.navtype

import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.livetyping.zarina.presentation.model.product.ProductOfferParcelable
import ru.livetyping.zarina.presentation.navigation.base.ParcelableListNavType

val NavType.Companion.ProductOfferParcelableListType: ProductOfferParcelableListNavType
    get() = ProductOfferParcelableListNavType()

class ProductOfferParcelableListNavType : ParcelableListNavType<ProductOfferParcelable>(
    isNullableAllowed = true,
    itemClass = ProductOfferParcelable::class,
    serializer = Json.serializersModule.serializer(),
)
