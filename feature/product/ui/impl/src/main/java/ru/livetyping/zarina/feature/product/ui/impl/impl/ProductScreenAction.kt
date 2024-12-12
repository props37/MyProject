package ru.livetyping.zarina.feature.product.ui.impl.impl

internal sealed interface ProductScreenAction {
    data object BackClicked : ProductScreenAction
}
