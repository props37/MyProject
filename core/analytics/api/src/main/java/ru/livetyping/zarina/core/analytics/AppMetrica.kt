package ru.livetyping.zarina.core.analytics

import ru.livetyping.zarina.core.analytics.model.CartProduct
import ru.livetyping.zarina.core.analytics.model.Category
import ru.livetyping.zarina.core.analytics.model.DeliveryMethodType
import ru.livetyping.zarina.core.analytics.model.AppliedFilters
import ru.livetyping.zarina.core.analytics.model.Order
import ru.livetyping.zarina.core.analytics.model.PaymentMethodType
import ru.livetyping.zarina.core.analytics.model.Product
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.analytics.model.SignInMethod

public interface AppMetrica {
    public fun reportScreenOpened(screen: Screen)

    public fun reportShowProductCardEvent(product: Product, screen: Screen)

    public fun reportProductScreenOpened(product: Product)

    public fun reportProductAddedToCart(product: CartProduct)

    public fun reportProductRemovedFromCart(product: CartProduct)

    public fun reportCheckoutStarted(products: List<CartProduct>)

    public fun reportProductAddedToWishlist(product: Product)

    public fun reportProductRemovedFromWishlist(product: Product)

    public fun reportPaymentMethodSelected(paymentMethodType: PaymentMethodType)

    public fun reportOrderConfirmed(order: Order)

    public fun reportDeliveryMethodSelected(deliveryMethodType: DeliveryMethodType)

    public fun reportProductSearch(query: String)

    public fun reportUserSignedUp()

    public fun reportUserSignedIn(method: SignInMethod)

    public fun reportProfileOpened(isUserSignedIn: Boolean)

    public fun reportPromoCodeApplied(promoCode: String)

    public fun reportBonusesUsed(bonusCount: Int)

    public fun reportProductFiltersApplied(category: Category, appliedFilters: AppliedFilters)

    public fun reportTokenRefreshAttempted(isSuccess: Boolean)

    public fun reportOrderCancelled(orderId: String)

    public fun reportError(identifier: String, message: String?, error: Throwable?)
}
