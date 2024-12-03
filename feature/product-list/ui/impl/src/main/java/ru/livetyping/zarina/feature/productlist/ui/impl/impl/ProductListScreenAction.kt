package ru.livetyping.zarina.feature.productlist.ui.impl.impl

internal sealed interface ProductListScreenAction {
    data object BackClicked : ProductListScreenAction
}
