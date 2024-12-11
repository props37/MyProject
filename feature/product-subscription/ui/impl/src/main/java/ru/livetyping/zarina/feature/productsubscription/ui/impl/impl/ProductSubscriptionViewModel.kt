package ru.livetyping.zarina.feature.productsubscription.ui.impl.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionNavEntry
import javax.inject.Inject

@HiltViewModel
internal class ProductSubscriptionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<ProductSubscriptionSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<ProductSubscriptionNavEntry>(
        typeMap = ProductSubscriptionNavEntry.typeMap(),
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProductSubscriptionScreenAction.BackClicked
            emitSideEffect(ProductSubscriptionSideEffect.Navigate(action))
        }
    }
}
