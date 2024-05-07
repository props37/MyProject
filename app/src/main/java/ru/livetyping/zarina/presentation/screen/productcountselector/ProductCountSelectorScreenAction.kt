package ru.livetyping.zarina.presentation.screen.productcountselector

sealed class ProductCountSelectorScreenAction {
    data object ScreenClosed : ProductCountSelectorScreenAction()

    data object CountChanged : ProductCountSelectorScreenAction()
}
