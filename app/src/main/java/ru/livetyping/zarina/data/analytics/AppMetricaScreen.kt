package ru.livetyping.zarina.data.analytics

import io.appmetrica.analytics.ecommerce.ECommerceScreen

// NOTE: DO NOT CHANGE. Values correspond to AppMetrica screen names. The same are used on iOS app.
enum class AppMetricaScreen(val screenName: String) {
    PRODUCT_LIST("product-list"),
    PRODUCT("product"),
    WISHLIST("wishlist"),
    PRODUCT_SEARCH("product-search");

    fun toECommerceScreen(): ECommerceScreen {
        return ECommerceScreen().apply {
            this.name = screenName
        }
    }
}
