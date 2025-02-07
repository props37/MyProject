package ru.livetyping.zarina.data.analytics

import io.appmetrica.analytics.ecommerce.ECommerceScreen
import ru.livetyping.zarina.domain.category.CategoryPath

sealed class AppMetricaScreen(val name: String) {
    fun toECommerceScreen(): ECommerceScreen {
        return ECommerceScreen().apply {
            this.name = name
        }
    }

    data object Home : AppMetricaScreen("HOME")

    data object Catalog : AppMetricaScreen("CATALOG")

    data class ProductList(val categoryPath: CategoryPath?) : AppMetricaScreen("PRODUCT_LIST")

    data object Product : AppMetricaScreen("PRODUCT")

    data object Wishlist : AppMetricaScreen("WISHLIST")

    data object Search : AppMetricaScreen("SEARCH")

    data object Cart : AppMetricaScreen("CART")
}
