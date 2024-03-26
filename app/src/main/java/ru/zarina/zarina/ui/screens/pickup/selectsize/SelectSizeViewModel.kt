package ru.zarina.zarina.ui.screens.pickup.selectsize

import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.old.Barcode
import ru.zarina.zarina.domain.old.Offer
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue

@KoinViewModel
class SelectSizeViewModel(
    private val interactor: SelectSizeInteractor,
) : ViewModel(),
    ISideEffectSource<SelectSizeViewModel.SideEffect> by SideEffectQueue() {

    fun onOfferClick(offer: Offer) {
        if (offer.isAvailable)
            sideEffect(SideEffect.GoBack)
        else
            sideEffect(SideEffect.ShowSubscribe(offer.barcode))
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
        data class ShowSubscribe(val offerBarcode: Barcode) : SideEffect
    }

}
