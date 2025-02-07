package ru.livetyping.zarina.data.analytics

import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.ecommerce.ECommerceAmount
import io.appmetrica.analytics.ecommerce.ECommerceCartItem
import io.appmetrica.analytics.ecommerce.ECommerceEvent
import io.appmetrica.analytics.ecommerce.ECommerceOrder
import io.appmetrica.analytics.ecommerce.ECommercePrice
import io.appmetrica.analytics.ecommerce.ECommerceProduct
import io.appmetrica.analytics.ecommerce.ECommerceScreen
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.category.CategoryPath
import ru.livetyping.zarina.domain.checkout.DeliveryMethod
import ru.livetyping.zarina.domain.checkout.PaymentMethod
import ru.livetyping.zarina.domain.common.Gender
import ru.livetyping.zarina.domain.order.OrderDetails
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.currentPrice
import java.util.UUID

object AppMetricaHelper {
    fun reportScreenOpened(screen: AppMetricaScreen) {
        val eCommerceScreen = ECommerceScreen().apply {
            name = screen.name
            if (screen is AppMetricaScreen.ProductList && screen.categoryPath != null) {
                categoriesPath = screen.categoryPath.getNameList()
            }
        }
        val event = ECommerceEvent.showScreenEvent(eCommerceScreen)
        AppMetrica.reportECommerce(event)

        when (screen) {
            is AppMetricaScreen.ProductList -> reportProductListOpened(screen)
            else -> Unit
        }
    }

    fun reportShowProductCardEvent(product: Product, screen: AppMetricaScreen) {
        val eCommerceProduct = getECommerceProduct(product)
        val eCommerceScreen = screen.toECommerceScreen()
        val event = ECommerceEvent.showProductCardEvent(eCommerceProduct, eCommerceScreen)
        AppMetrica.reportECommerce(event)
    }

    fun reportProductScreenOpened(product: Product) {
        val eCommerceProduct = getECommerceProduct(product)
        val event = ECommerceEvent.showProductDetailsEvent(eCommerceProduct, null)
        AppMetrica.reportECommerce(event)
    }

    fun reportProductAddedToCart(product: Product, count: Int) {
        val eCommerceProduct = getECommerceProduct(product)
        val revenue = eCommerceProduct.actualPrice ?: getECommerceCurrentPrice(product)
        val quantity = count.toLong()
        val cartItem = ECommerceCartItem(eCommerceProduct, revenue, quantity)
        val event = ECommerceEvent.addCartItemEvent(cartItem)
        AppMetrica.reportECommerce(event)
    }

    fun reportProductRemovedFromCart(product: CartProduct) {
        val eCommerceProduct = getECommerceProduct(product)
        val revenue = eCommerceProduct.actualPrice ?: getECommerceCurrentPrice(product)
        val quantity = product.count.toLong()
        val cartItem = ECommerceCartItem(eCommerceProduct, revenue, quantity)
        val event = ECommerceEvent.removeCartItemEvent(cartItem)
        AppMetrica.reportECommerce(event)
    }

    fun reportCheckoutStarted(cart: Cart) {
        val identifier = UUID.randomUUID().toString()
        val cartItems = cart.products.map { product ->
            val eCommerceProduct = getECommerceProduct(product)
            val revenue = eCommerceProduct.actualPrice
                ?: ECommercePrice(getECommerceAmount(product.price.currentPrice))
            val quantity = product.count.toLong()
            ECommerceCartItem(eCommerceProduct, revenue, quantity)
        }
        val order = ECommerceOrder(identifier, cartItems)
        val event = ECommerceEvent.beginCheckoutEvent(order)
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

    fun reportOrderConfirmed(order: OrderDetails) {
        val identifier = order.id.value.toString()
        val eCommerceCartItems = order.products.map { product ->
            val eCommerceProduct = getECommerceProduct(
                productId = product.id,
                productName = product.name,
                currentPrice = product.price.currentPrice,
                originalPrice = product.price.originalPrice,
            )
            val revenue = eCommerceProduct.actualPrice
                ?: ECommercePrice(getECommerceAmount(product.price.currentPrice))
            val quantity = product.count.toLong()
            ECommerceCartItem(eCommerceProduct, revenue, quantity)
        }
        val eCommerceOrder = ECommerceOrder(identifier, eCommerceCartItems)
        val event = ECommerceEvent.purchaseEvent(eCommerceOrder)
        AppMetrica.reportECommerce(event)
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

    fun reportPromoCodeApplied(promoCode: String) {
        val parameters = mapOf(KEY_PROMO_CODE to promoCode)
        AppMetrica.reportEvent(EVENT_APPLY_PROMO_CODE, parameters)
    }

    fun reportBonusesUsed(bonusCount: Int) {
        val parameters = mapOf(KEY_BONUS_COUNT to bonusCount)
        AppMetrica.reportEvent(EVENT_USE_BONUSES, parameters)
    }

    private fun reportProductListOpened(screen: AppMetricaScreen.ProductList) {
        val parameters = screen.categoryPath?.let { path ->
            val categoryPathNameList = path.getNameList().takeIf { it.isNotEmpty() }
            categoryPathNameList?.let { nameList ->
                mapOf(
                    KEY_CATEGORY to nameList.last(),
                    KEY_CATEGORY_PATH to categoryPathNameList,
                )
            }
        }
        AppMetrica.reportEvent(EVENT_OPEN_PRODUCT_LIST, parameters)
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
        return ECommerceAmount(price.toLong(), CURRENCY_UNIT_RUB)
    }

    private fun CategoryPath.getNameList(): List<String> {
        val path = this
        return buildList {
            val section = when (path.gender) {
                Gender.FEMALE -> CATEGORY_WOMEN
                Gender.MALE -> CATEGORY_MEN
            }
            add(section)
            addAll(path.path.map { it.name })
        }
    }

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

    private const val CURRENCY_UNIT_RUB = "RUB"

    private const val CATEGORY_WOMEN = "Женщинам"
    private const val CATEGORY_MEN = "Мужчинам"
}
