package ru.zarina.zarina.ui.screens.pickup.selectsize

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.domain.Barcode
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class SelectSizeViewModel @Inject constructor(
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
