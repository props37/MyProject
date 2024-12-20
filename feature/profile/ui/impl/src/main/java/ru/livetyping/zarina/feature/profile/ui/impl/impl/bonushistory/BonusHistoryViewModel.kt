package ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import javax.inject.Inject

@HiltViewModel
internal class BonusHistoryViewModel @Inject constructor(

) : ViewModel(), SideEffectSource<BonusHistorySideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = BonusHistoryScreenAction.BackClicked
            emitSideEffect(BonusHistorySideEffect.Navigate(action))
        }
    }
}
