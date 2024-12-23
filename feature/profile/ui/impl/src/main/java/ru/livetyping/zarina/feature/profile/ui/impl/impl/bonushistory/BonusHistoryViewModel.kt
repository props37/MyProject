package ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.model.BonusHistoryTab
import javax.inject.Inject

@HiltViewModel
internal class BonusHistoryViewModel @Inject constructor(

) : ViewModel(), SideEffectSource<BonusHistorySideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val currentTab = MutableStateFlow(BonusHistoryTab.BONUS_HISTORY)

    val tabRowState: StateFlow<TabRowState<BonusHistoryTab>> = currentTab.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
    ) { currentTab ->
        TabRowState(
            tabs = BonusHistoryTab.entries.toImmutableList(),
            currentTab = currentTab,
        )
    }

    fun onTabRowEvent(event: TabRowEvent<BonusHistoryTab>) {
        when (event) {
            is TabRowEvent.TabChanged -> currentTab.value = event.tab
            is TabRowEvent.TabReselected -> Unit
        }
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = BonusHistoryScreenAction.BackClicked
            emitSideEffect(BonusHistorySideEffect.Navigate(action))
        }
    }
}
