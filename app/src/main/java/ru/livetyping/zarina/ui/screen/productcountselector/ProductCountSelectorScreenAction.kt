package ru.livetyping.zarina.ui.screen.productcountselector

sealed class ProductCountSelectorScreenAction {
    data object ScreenClosed : ProductCountSelectorScreenAction()

    data object CountChanged : ProductCountSelectorScreenAction()
}
