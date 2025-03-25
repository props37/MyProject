package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.parcelableListNavType
import ru.livetyping.zarina.core.navigationutil.parcelableNavType
import ru.livetyping.zarina.core.uimodel.cart.CartProductParcelable
import ru.livetyping.zarina.core.uimodel.cart.CartTypeParcelable
import ru.livetyping.zarina.core.uimodel.checkout.DeliveryMethodParcelable
import ru.livetyping.zarina.core.uimodel.checkout.RecipientParcelable
import ru.livetyping.zarina.core.uimodel.geo.CityParcelable
import ru.livetyping.zarina.core.uimodel.store.StoreParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
internal class SelectedPickupStoreNavEntry private constructor(
    val cartType: CartTypeParcelable,
    val checkoutStep: Int,
    val recipient: RecipientParcelable,
    val deliveryMethod: DeliveryMethodParcelable,
    val city: CityParcelable,
    val store: StoreParcelable,
    val availableProducts: List<CartProductParcelable>,
) : NavigationEntry {
    companion object {
        fun from(
            cartType: CartType,
            checkoutStep: Int,
            recipient: Recipient,
            deliveryMethod: DeliveryMethod,
            city: City,
            store: Store,
            availableProducts: List<CartProduct>,
        ): SelectedPickupStoreNavEntry {
            return SelectedPickupStoreNavEntry(
                cartType = CartTypeParcelable.from(cartType),
                checkoutStep = checkoutStep,
                recipient = RecipientParcelable.from(recipient),
                deliveryMethod = DeliveryMethodParcelable.from(deliveryMethod),
                city = CityParcelable.from(city),
                store = StoreParcelable.from(store),
                availableProducts = availableProducts.map { CartProductParcelable.from(it) },
            )
        }

        fun typeMap(): Map<KType, NavType<*>> {
            val recipientType = parcelableNavType<RecipientParcelable>(
                isNullableAllowed = false,
            )
            val deliveryMethodType = parcelableNavType<DeliveryMethodParcelable>(
                isNullableAllowed = false,
            )
            val cityType = parcelableNavType<CityParcelable>(isNullableAllowed = false)
            val storeType = parcelableNavType<StoreParcelable>(isNullableAllowed = false)
            val availableProductListType = parcelableListNavType<CartProductParcelable>(
                isNullableAllowed = false,
            )
            return mapOf(
                typeOf<CartTypeParcelable>() to NavType.EnumType(CartTypeParcelable::class.java),
                typeOf<RecipientParcelable>() to recipientType,
                typeOf<DeliveryMethodParcelable>() to deliveryMethodType,
                typeOf<CityParcelable>() to cityType,
                typeOf<StoreParcelable>() to storeType,
                typeOf<List<CartProductParcelable>>() to availableProductListType,
            )
        }
    }
}
