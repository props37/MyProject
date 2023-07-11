package ru.zarina.zarina.ui.screens.home

import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue

@KoinViewModel
class HomeViewModel(
    private val interactor: HomeInteractor,
) : ViewModel(),
    ISideEffectSource<HomeViewModel.SideEffect> by SideEffectQueue() {

    fun onProductClick() {
        sideEffect(SideEffect.ShowProduct(Product.Id("1329404704-50")))
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        class ShowProduct(val id: Product.Id) : SideEffect
    }

}
