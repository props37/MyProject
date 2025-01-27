package ru.livetyping.zarina.feature.productsubscription.ui.api

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.ParcelableNavType
import ru.livetyping.zarina.core.uimodel.product.ProductOfferParcelable
import ru.livetyping.zarina.core.uimodel.product.ProductShortParcelable
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionFeature.NavActions
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionFeature.NavEntry
import kotlin.reflect.KType
import kotlin.reflect.typeOf

public interface ProductSubscriptionFeature :
    ComposableFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public class NavEntry private constructor(
        public val product: ProductShortParcelable,
        public val offer: ProductOfferParcelable,
    ) : NavigationEntry {
        public companion object {
            public fun create(product: Product, offer: ProductOffer): NavEntry {
                return NavEntry(
                    product = ProductShortParcelable.from(product),
                    offer = ProductOfferParcelable.from(offer),
                )
            }

            public fun typeMap(): Map<KType, NavType<*>> {
                val productType = ParcelableNavType<ProductShortParcelable?>(
                    isNullableAllowed = true,
                    serializer = kotlinx.serialization.serializer(),
                )
                val offerType = ParcelableNavType<ProductOfferParcelable?>(
                    isNullableAllowed = true,
                    serializer = kotlinx.serialization.serializer(),
                )
                return mapOf(
                    typeOf<ProductShortParcelable>() to productType,
                    typeOf<ProductOfferParcelable>() to offerType,
                )
            }
        }
    }

    public class NavActions(
        public val onBackClicked: () -> Unit,
        public val onSubscriptionCompleted: () -> Unit,
    ) : NavigationActions
}
