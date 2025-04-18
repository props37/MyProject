package ru.livetyping.zarina.core.analytics.model

public sealed class Screen(public val name: String) {
    public data object Home : Screen("HOME")

    public data object Catalog : Screen("CATALOG")

    public data class ProductList(val categoryPath: CategoryPath?) :
        Screen("PRODUCT_LIST")

    public data object Product : Screen("PRODUCT")

    public data object Wishlist : Screen("WISHLIST")

    public data object Search : Screen("SEARCH")

    public data object Cart : Screen("CART")
}
