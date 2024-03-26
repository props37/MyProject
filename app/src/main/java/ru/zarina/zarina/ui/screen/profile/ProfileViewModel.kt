package ru.zarina.zarina.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.zarina.zarina.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.ui.screen.profile.ProfileViewModel.SideEffect
import ru.zarina.zarina.util.base.usecase.invoke
import ru.zarina.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val interactor: ProfileInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    // TODO: [High] Display MyOrders only to authorized users
    val infoItems: StateFlow<ImmutableList<InfoItem>> =
        MutableStateFlow(InfoItem.entries.toImmutableList()).asStateFlow()

    val city: StateFlow<City?> = interactor.getUserCityFlow()
        .map { result ->
            result.getOrDefault(City.DEFAULT)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null
        )

    fun onInfoItemClicked(item: InfoItem) {
        // TODO: [High] Implement
    }

    sealed interface SideEffect : SideEffectSource.SideEffect

    enum class InfoItem { MyOrders, City, Shops, Help, AboutCompany }
}
