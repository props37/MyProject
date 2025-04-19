package ru.livetyping.zarina.core.analytics.impl

import io.appmetrica.analytics.ecommerce.ECommerceAmount
import io.appmetrica.analytics.ecommerce.ECommerceCartItem
import io.appmetrica.analytics.ecommerce.ECommerceEvent
import io.appmetrica.analytics.ecommerce.ECommerceOrder
import io.appmetrica.analytics.ecommerce.ECommercePrice
import io.appmetrica.analytics.ecommerce.ECommerceProduct
import io.appmetrica.analytics.ecommerce.ECommerceScreen
import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.analytics.impl.util.toECommerceScreen
import ru.livetyping.zarina.core.analytics.impl.util.toNameList
import ru.livetyping.zarina.core.analytics.model.AppliedFilters
import ru.livetyping.zarina.core.analytics.model.CartProduct
import ru.livetyping.zarina.core.analytics.model.Category
import ru.livetyping.zarina.core.analytics.model.DeliveryMethodType
import ru.livetyping.zarina.core.analytics.model.Order
import ru.livetyping.zarina.core.analytics.model.PaymentMethodType
import ru.livetyping.zarina.core.analytics.model.Product
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.analytics.model.SignInMethod
import java.math.BigDecimal
import java.util.UUID
import io.appmetrica.analytics.AppMetrica as AppMetricaInstance

public class AppMetricaImpl : AppMetrica {
    override fun reportScreenOpened(screen: Screen) {
        val eCommerceScreen = ECommerceScreen().apply {
            name = screen.name
            if (screen is Screen.ProductList && screen.categoryPath != null) {
                screen.categoryPath?.let {
                    val nameList = it.toNameList()
                    categoriesPath = nameList
                }
            }
        }
        val event = ECommerceEvent.showScreenEvent(eCommerceScreen)
        AppMetricaInstance.reportECommerce(event)

        when (screen) {
            is Screen.ProductList -> reportProductListOpened(screen)
            Screen.Cart -> reportCartOpened()
            else -> Unit
        }
    }

    override fun reportShowProductCardEvent(product: Product, screen: Screen) {
        val eCommerceProduct = getECommerceProduct(product)
        val eCommerceScreen = screen.toECommerceScreen()
        val event = ECommerceEvent.showProductCardEvent(eCommerceProduct, eCommerceScreen)
        AppMetricaInstance.reportECommerce(event)
    }

    override fun reportProductScreenOpened(product: Product) {
        val eCommerceProduct = getECommerceProduct(product)
        val event = ECommerceEvent.showProductDetailsEvent(eCommerceProduct, null)
        AppMetricaInstance.reportECommerce(event)
    }

    override fun reportProductAddedToCart(product: CartProduct) {
        val eCommerceProduct = getECommerceProduct(product.product)
        val revenue =
            eCommerceProduct.actualPrice ?: getECommercePrice(product.product.currentPrice)
        val quantity = product.count.toLong()
        val cartItem = ECommerceCartItem(eCommerceProduct, revenue, quantity)
        val event = ECommerceEvent.addCartItemEvent(cartItem)
        AppMetricaInstance.reportECommerce(event)
    }

    override fun reportProductRemovedFromCart(product: CartProduct) {
        val eCommerceProduct = getECommerceProduct(product.product)
        val revenue =
            eCommerceProduct.actualPrice ?: getECommercePrice(product.product.currentPrice)
        val quantity = product.count.toLong()
        val cartItem = ECommerceCartItem(eCommerceProduct, revenue, quantity)
        val event = ECommerceEvent.removeCartItemEvent(cartItem)
        AppMetricaInstance.reportECommerce(event)
    }

    override fun reportCheckoutStarted(products: List<CartProduct>) {
        val identifier = UUID.randomUUID().toString()
        val cartItems = products.map { product ->
            val eCommerceProduct = getECommerceProduct(product.product)
            val revenue = eCommerceProduct.actualPrice
                ?: getECommercePrice(product.product.currentPrice)
            val quantity = product.count.toLong()
            ECommerceCartItem(eCommerceProduct, revenue, quantity)
        }
        val order = ECommerceOrder(identifier, cartItems)
        val event = ECommerceEvent.beginCheckoutEvent(order)
        AppMetricaInstance.reportECommerce(event)
    }

    override fun reportProductAddedToWishlist(product: Product) {
        val parameters = mapOf(
            KEY_SKU to product.id,
            KEY_NAME to product.name,
        )
        AppMetricaInstance.reportEvent(EVENT_ADD_WISHLIST_ITEM, parameters)
    }

    override fun reportProductRemovedFromWishlist(product: Product) {
        val parameters = mapOf(
            KEY_SKU to product.id,
            KEY_NAME to product.name,
        )
        AppMetricaInstance.reportEvent(EVENT_REMOVE_WISHLIST_ITEM, parameters)
    }

    override fun reportPaymentMethodSelected(paymentMethodType: PaymentMethodType) {
        val parameters = mapOf(KEY_PAYMENT_METHOD to paymentMethodType.typeName)
        AppMetricaInstance.reportEvent(EVENT_SELECT_PAYMENT_METHOD, parameters)
    }

    override fun reportOrderConfirmed(order: Order) {
        val eCommerceCartItems = order.products.map { product ->
            val eCommerceProduct = getECommerceProduct(product.product)
            val revenue = eCommerceProduct.actualPrice
                ?: ECommercePrice(getECommerceAmount(product.product.currentPrice))
            val quantity = product.count.toLong()
            ECommerceCartItem(eCommerceProduct, revenue, quantity)
        }
        val eCommerceOrder = ECommerceOrder(order.id, eCommerceCartItems)
        val event = ECommerceEvent.purchaseEvent(eCommerceOrder)
        AppMetricaInstance.reportECommerce(event)
    }

    override fun reportDeliveryMethodSelected(deliveryMethodType: DeliveryMethodType) {
        val parameters = mapOf(KEY_DELIVERY_TYPE to deliveryMethodType.typeName)
        AppMetricaInstance.reportEvent(EVENT_SELECT_DELIVERY_TYPE, parameters)
    }

    override fun reportProductSearch(query: String) {
        val parameters = mapOf(KEY_QUERY to query)
        AppMetricaInstance.reportEvent(EVENT_SEARCH_PRODUCTS, parameters)
    }

    override fun reportUserSignedUp() {
        AppMetricaInstance.reportEvent(EVENT_SIGN_UP)
    }

    override fun reportUserSignedIn(method: SignInMethod) {
        val parameters = mapOf(KEY_SIGN_IN_METHOD to method.methodName)
        AppMetricaInstance.reportEvent(EVENT_SIGN_IN, parameters)
    }

    override fun reportProfileOpened(isUserSignedIn: Boolean) {
        if (isUserSignedIn) {
            AppMetricaInstance.reportEvent(EVENT_OPEN_PROFILE)
        }
    }

    override fun reportPromoCodeApplied(promoCode: String) {
        val parameters = mapOf(KEY_PROMO_CODE to promoCode)
        AppMetricaInstance.reportEvent(EVENT_APPLY_PROMO_CODE, parameters)
    }

    override fun reportBonusesUsed(bonusCount: Int) {
        val parameters = mapOf(KEY_BONUS_COUNT to bonusCount)
        AppMetricaInstance.reportEvent(EVENT_USE_BONUSES, parameters)
    }

    override fun reportProductFiltersApplied(category: Category, appliedFilters: AppliedFilters) {
        val filterParameters = buildMap {
            appliedFilters.sorting?.getName()?.let { put(KEY_SORTING, it) }

            if (appliedFilters.price?.first != null || appliedFilters.price?.last != null) {
                val map = buildMap {
                    appliedFilters.price?.first?.let { put(KEY_MIN, it) }
                    appliedFilters.price?.last?.let { put(KEY_MAX, it) }
                }
                put(KEY_PRICE, map)
            }

            appliedFilters.materials?.takeIf { it.isNotEmpty() }
                ?.let { items ->
                    val names = items.map { it.name }
                    put(KEY_MATERIALS, names)
                }

            appliedFilters.sizes?.takeIf { it.isNotEmpty() }
                ?.let { items ->
                    val names = items.map { it.name }
                    put(KEY_SIZES, names)
                }

            appliedFilters.colors?.takeIf { it.isNotEmpty() }
                ?.let { items ->
                    val names = items.map { it.name }
                    put(KEY_COLORS, names)
                }

            appliedFilters.isDeliveryAvailable?.takeIf { it }
                ?.let { put(KEY_DELIVERY_AVAILABILITY, it) }

            appliedFilters.isStorePickupAvailable?.takeIf { it }
                ?.let { put(KEY_STORE_PICKUP_AVAILABILITY, it) }

            appliedFilters.pickupStores?.takeIf { it.isNotEmpty() }
                ?.let { items ->
                    val names = items.map { it.name }
                    put(KEY_PICKUP_STORES, names)
                }
        }
        val parameters = buildMap {
            put(KEY_CATEGORY, category.name)
            put(KEY_FILTERS, filterParameters)
        }
        AppMetricaInstance.reportEvent(EVENT_APPLY_PRODUCT_FILTERS, parameters)
    }

    override fun reportTokenRefreshAttempted(isSuccess: Boolean) {
        val parameters = mapOf(KEY_IS_SUCCESS to isSuccess)
        AppMetricaInstance.reportEvent(EVENT_REFRESH_TOKENS, parameters)
    }

    override fun reportOrderCancelled(orderId: String) {
        val parameters = mapOf(KEY_ID to orderId)
        AppMetricaInstance.reportEvent(EVENT_CANCEL_ORDER, parameters)
    }

    override fun reportError(identifier: String, message: String?, error: Throwable?) {
        AppMetricaInstance.reportError(identifier, message, error)
    }

    private fun reportProductListOpened(screen: Screen.ProductList) {
        val parameters = screen.categoryPath?.let { path ->
            val categoryPathNameList = path.toNameList().takeIf { it.isNotEmpty() }
            categoryPathNameList?.let { nameList ->
                mapOf(
                    KEY_CATEGORY to nameList.last(),
                    KEY_CATEGORY_PATH to categoryPathNameList,
                )
            }
        }
        AppMetricaInstance.reportEvent(EVENT_OPEN_PRODUCT_LIST, parameters)
    }

    private fun reportCartOpened() {
        AppMetricaInstance.reportEvent(EVENT_OPEN_CART)
    }

    private fun AppliedFilters.Sorting.getName(): String {
        return when (this) {
            AppliedFilters.Sorting.NEW -> SORTING_NEW
            AppliedFilters.Sorting.POPULAR -> SORTING_POPULAR
            AppliedFilters.Sorting.DISCOUNT -> SORTING_DISCOUNT
            AppliedFilters.Sorting.PRICE_LOW_TO_HIGH -> SORTING_PRICE_LOW_TO_HIGH
            AppliedFilters.Sorting.PRICE_HIGH_TO_LOW -> SORTING_PRICE_HIGH_TO_LOW
        }
    }

    private fun getECommerceProduct(product: Product): ECommerceProduct {
        return ECommerceProduct(product.id).apply {
            name = product.name
            actualPrice = getECommercePrice(product.currentPrice)
            originalPrice = getECommercePrice(product.originalPrice)
        }
    }

    private fun getECommercePrice(price: BigDecimal): ECommercePrice {
        return ECommercePrice(getECommerceAmount(price))
    }

    private fun getECommerceAmount(price: BigDecimal): ECommerceAmount {
        return ECommerceAmount(price, CURRENCY_UNIT_RUB)
    }

    private companion object {
        private const val EVENT_ADD_WISHLIST_ITEM = "addWishlistItem"
        private const val EVENT_REMOVE_WISHLIST_ITEM = "removeWishlistItem"
        private const val EVENT_SELECT_PAYMENT_METHOD = "selectPaymentMethod"
        private const val EVENT_SELECT_DELIVERY_TYPE = "selectDeliveryType"
        private const val EVENT_SEARCH_PRODUCTS = "searchProducts"
        private const val EVENT_OPEN_CART = "openCart"
        private const val EVENT_SIGN_UP = "signUp"
        private const val EVENT_SIGN_IN = "signIn"
        private const val EVENT_OPEN_PROFILE = "openProfile"
        private const val EVENT_APPLY_PROMO_CODE = "applyPromoCode"
        private const val EVENT_USE_BONUSES = "useBonuses"
        private const val EVENT_OPEN_PRODUCT_LIST = "openProductList"
        private const val EVENT_APPLY_PRODUCT_FILTERS = "applyProductFilters"
        private const val EVENT_CANCEL_ORDER = "cancelOrder"
        private const val EVENT_REFRESH_TOKENS = "_refreshTokens"

        private const val KEY_SKU = "sku"
        private const val KEY_NAME = "name"
        private const val KEY_DELIVERY_TYPE = "deliveryType"
        private const val KEY_SIGN_IN_METHOD = "method"
        private const val KEY_PAYMENT_METHOD = "paymentMethod"
        private const val KEY_QUERY = "query"
        private const val KEY_PROMO_CODE = "promoCode"
        private const val KEY_BONUS_COUNT = "bonusCount"
        private const val KEY_CATEGORY = "category"
        private const val KEY_CATEGORY_PATH = "categoryPath"
        private const val KEY_FILTERS = "filters"
        private const val KEY_SORTING = "sorting"
        private const val KEY_PRICE = "price"
        private const val KEY_MIN = "min"
        private const val KEY_MAX = "max"
        private const val KEY_MATERIALS = "materials"
        private const val KEY_SIZES = "sizes"
        private const val KEY_COLORS = "colors"
        private const val KEY_DELIVERY_AVAILABILITY = "deliveryAvailability"
        private const val KEY_STORE_PICKUP_AVAILABILITY = "storePickupAvailability"
        private const val KEY_PICKUP_STORES = "pickupStores"
        private const val KEY_IS_SUCCESS = "isSuccess"
        private const val KEY_ID = "id"

        private const val CURRENCY_UNIT_RUB = "RUB"

        private const val SORTING_NEW = "NEW"
        private const val SORTING_POPULAR = "POPULAR"
        private const val SORTING_DISCOUNT = "DISCOUNT_SIZE"
        private const val SORTING_PRICE_LOW_TO_HIGH = "PRICE_LOW_TO_HIGH"
        private const val SORTING_PRICE_HIGH_TO_LOW = "PRICE_HIGH_TO_LOW"
    }
}
