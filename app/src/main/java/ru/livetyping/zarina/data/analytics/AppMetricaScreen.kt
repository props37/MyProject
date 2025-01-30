package ru.livetyping.zarina.data.analytics

import io.appmetrica.analytics.ecommerce.ECommerceScreen

// NOTE: DO NOT CHANGE. Values correspond to AppMetrica screen names. The same are used on iOS app.
enum class AppMetricaScreen {
    PRODUCT_LIST,
    PRODUCT,
    WISHLIST,
    PRODUCT_SEARCH;

    fun toECommerceScreen(): ECommerceScreen {
        return ECommerceScreen().apply {
            this.name = this@AppMetricaScreen.name
        }
    }
}
