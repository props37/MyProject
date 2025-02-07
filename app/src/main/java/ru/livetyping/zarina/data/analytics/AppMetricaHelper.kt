package ru.livetyping.zarina.data.analytics

import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.ecommerce.ECommerceAmount
import io.appmetrica.analytics.ecommerce.ECommerceCartItem
import io.appmetrica.analytics.ecommerce.ECommerceEvent
import io.appmetrica.analytics.ecommerce.ECommerceOrder
import io.appmetrica.analytics.ecommerce.ECommercePrice
import io.appmetrica.analytics.ecommerce.ECommerceProduct
import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.checkout.DeliveryMethod
import ru.livetyping.zarina.domain.checkout.PaymentMethod
import ru.livetyping.zarina.domain.order.OrderDetails
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.currentPrice

object AppMetricaHelper {
    fun reportShowProductCardEvent(product: Product, screen: AppMetricaScreen) {
        val eCommerceProduct = getECommerceProduct(product)
        val eCommerceScreen = screen.toECommerceScreen()
        val event = ECommerceEvent.showProductCardEvent(eCommerceProduct, eCommerceScreen)
        AppMetrica.reportECommerce(event)
    }

    fun reportShowProductDetailsEvent(product: Product) {
        val eCommerceProduct = getECommerceProduct(product)
        val event = ECommerceEvent.showProductDetailsEvent(eCommerceProduct, null)
        AppMetrica.reportECommerce(event)
    }

    fun reportAddCartItemEvent(product: Product, count: Int) {
        val eCommerceProduct = getECommerceProduct(product)
        val cartItem = ECommerceCartItem(
            /* product = */ eCommerceProduct,
            /* revenue = */ eCommerceProduct.actualPrice ?: getECommerceCurrentPrice(product),
            /* quantityMicros = */ count.toLong()
        )
        val event = ECommerceEvent.addCartItemEvent(cartItem)
        AppMetrica.reportECommerce(event)
    }

    fun reportRemoveCartItemEvent(product: CartProduct) {
        val eCommerceProduct = getECommerceProduct(product)
        val cartItem = ECommerceCartItem(
            /* product = */ eCommerceProduct,
            /* revenue = */ eCommerceProduct.actualPrice ?: getECommerceCurrentPrice(product),
            /* quantityMicros = */ product.count.toLong(),
        )
        val event = ECommerceEvent.removeCartItemEvent(cartItem)
        AppMetrica.reportECommerce(event)
    }

    fun reportProductAddedToWishlist(product: Product) {
        val parameters = mapOf(
            KEY_SKU to product.id.value,
            KEY_NAME to product.name,
        )
        AppMetrica.reportEvent(EVENT_ADD_WISHLIST_ITEM, parameters)
    }

    fun reportProductAddedToWishlist(productId: Product.Id, productName: String) {
        val parameters = mapOf(
            KEY_SKU to productId.value,
            KEY_NAME to productName,
        )
        AppMetrica.reportEvent(EVENT_ADD_WISHLIST_ITEM, parameters)
    }

    fun reportProductRemovedFromWishlist(product: Product) {
        val parameters = mapOf(
            KEY_SKU to product.id.value,
            KEY_NAME to product.name,
        )
        AppMetrica.reportEvent(EVENT_REMOVE_WISHLIST_ITEM, parameters)
    }

    fun reportProductRemovedFromWishlist(productId: Product.Id, productName: String) {
        val parameters = mapOf(
            KEY_SKU to productId.value,
            KEY_NAME to productName,
        )
        AppMetrica.reportEvent(EVENT_REMOVE_WISHLIST_ITEM, parameters)
    }

    fun reportPaymentMethodSelected(paymentMethod: PaymentMethod) {
        val parameters = mapOf(KEY_PAYMENT_METHOD to paymentMethod.title)
        AppMetrica.reportEvent(EVENT_SELECT_PAYMENT_METHOD, parameters)
    }

    fun reportCartOpened() {
        AppMetrica.reportEvent(EVENT_OPEN_CART)
    }

    fun reportStartCheckoutEvent() {
        AppMetrica.reportEvent(EVENT_START_CHECKOUT)
    }

    fun reportCompletePurchaseEvent(order: OrderDetails) {
        val eCommerceCartItems = order.products.map { product ->
            val eCommerceProduct = getECommerceProduct(
                productId = product.id,
                productName = product.name,
                currentPrice = product.price.currentPrice,
                originalPrice = product.price.originalPrice,
            )
            ECommerceCartItem(
                /* product = */ eCommerceProduct,
                /* revenue = */ ECommercePrice(getECommerceAmount(product.price.currentPrice)),
                /* quantityMicros = */ product.count.toLong(),
            )
        }
        val eCommerceOrder = ECommerceOrder(
            /* identifier = */ order.id.value.toString(),
            /* cartItems = */ eCommerceCartItems,
        )
        val event = ECommerceEvent.purchaseEvent(eCommerceOrder)
        AppMetrica.reportECommerce(event)
    }

    // TODO: [Top] Rename
    fun reportPaymentTypeEvent() {
        AppMetrica.reportEvent(EVENT_PAYMENT_TYPE)
    }

    fun reportDeliveryMethodSelected(deliveryMethod: DeliveryMethod) {
        val parameters = mapOf(KEY_DELIVERY_TYPE to deliveryMethod.name)
        AppMetrica.reportEvent(EVENT_SELECT_DELIVERY_TYPE, parameters)
    }

    fun reportProductSearch(query: String) {
        val parameters = mapOf(KEY_QUERY to query)
        AppMetrica.reportEvent(EVENT_SEARCH_PRODUCTS, parameters)
    }

    fun reportUserSignedUp() {
        AppMetrica.reportEvent(EVENT_SIGN_UP)
    }

    fun reportUserSignedIn(method: AppMetricaSignInMethod) {
        val parameters = mapOf(KEY_SIGN_IN_METHOD to method.value)
        AppMetrica.reportEvent(EVENT_SIGN_IN, parameters)
    }

    fun reportProfileOpened(isUserSignedIn: Boolean) {
        if (isUserSignedIn) {
            AppMetrica.reportEvent(EVENT_OPEN_PROFILE)
        }
    }

    private fun getECommerceProduct(product: Product): ECommerceProduct {
        return ECommerceProduct(product.id.value).apply {
            name = product.name
            actualPrice = getECommerceCurrentPrice(product)
            originalPrice = getECommerceOriginalPrice(product)
        }
    }

    private fun getECommerceProduct(product: CartProduct): ECommerceProduct {
        return ECommerceProduct(product.productId.value).apply {
            name = product.name
            actualPrice = getECommerceCurrentPrice(product)
            originalPrice = getECommerceOriginalPrice(product)
        }
    }

    private fun getECommerceProduct(
        productId: Product.Id,
        productName: String,
        currentPrice: Int,
        originalPrice: Int,
    ): ECommerceProduct {
        return ECommerceProduct(productId.value).apply {
            name = productName
            actualPrice = ECommercePrice(getECommerceAmount(currentPrice))
            this.originalPrice = ECommercePrice(getECommerceAmount(originalPrice))
        }
    }

    private fun getECommerceCurrentPrice(product: Product): ECommercePrice {
        return ECommercePrice(getECommerceAmount(product.price.currentPrice))
    }

    private fun getECommerceCurrentPrice(product: CartProduct): ECommercePrice {
        return ECommercePrice(getECommerceAmount(product.price.currentPrice))
    }

    private fun getECommerceOriginalPrice(product: Product): ECommercePrice {
        return ECommercePrice(getECommerceAmount(product.price.originalPrice))
    }

    private fun getECommerceOriginalPrice(product: CartProduct): ECommercePrice {
        return ECommercePrice(getECommerceAmount(product.price.originalPrice))
    }

    private fun getECommerceAmount(price: Int): ECommerceAmount {
        return ECommerceAmount(price.toLong(), RUB_UNIT)
    }

    private const val EVENT_ADD_WISHLIST_ITEM = "addWishlistItem"
    private const val EVENT_REMOVE_WISHLIST_ITEM = "removeWishlistItem"
    private const val EVENT_SELECT_PAYMENT_METHOD = "selectPaymentMethod"
    private const val EVENT_SELECT_DELIVERY_TYPE = "selectDeliveryType"
    private const val EVENT_SEARCH_PRODUCTS = "searchProducts"
    private const val EVENT_OPEN_CART = "openCart"
    private const val EVENT_START_CHECKOUT = "beginOrder"
    private const val EVENT_PAYMENT_TYPE = "paymentType"
    private const val EVENT_SIGN_UP = "signUp"
    private const val EVENT_SIGN_IN = "signIn"
    private const val EVENT_OPEN_PROFILE = "openProfile"

    private const val KEY_SKU = "sku"
    private const val KEY_NAME = "name"
    private const val KEY_DELIVERY_TYPE = "deliveryType"
    private const val KEY_SIGN_IN_METHOD = "method"
    private const val KEY_PAYMENT_METHOD = "paymentMethod"
    private const val KEY_QUERY = "query"

    private const val RUB_UNIT = "RUB"
}
