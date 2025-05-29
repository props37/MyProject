package ru.livetyping.zarina.core.uikitpaging.product

public sealed interface ProductGridSideEffect {
    public data class ScrollToTop(val animate: Boolean) : ProductGridSideEffect
}
