package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration

internal sealed interface FiltrationScreenAction {
    data object BackClicked : FiltrationScreenAction
}
