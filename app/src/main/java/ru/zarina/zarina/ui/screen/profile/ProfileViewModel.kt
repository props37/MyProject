package ru.zarina.zarina.ui.screen.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.base.throttler.Throttler
import ru.zarina.zarina.domain.common.Url
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.ui.base.text.Text
import ru.zarina.zarina.ui.common.util.ScreenResultHandler
import ru.zarina.zarina.ui.common.util.getNavigationThrottler
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.screen.profile.ProfileViewModel.SideEffect
import ru.zarina.zarina.usecase.user.SetUserCityUseCase
import ru.zarina.zarina.util.base.usecase.invoke
import ru.zarina.zarina.util.library.coroutines.WhileUiSubscribed

@HiltViewModel(assistedFactory = ProfileViewModel.Factory::class)
class ProfileViewModel @AssistedInject constructor(
    @Assisted
    backStackEntrySavedStateHandle: SavedStateHandle,
    savedStateHandle: SavedStateHandle,
    private val interactor: ProfileInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val screenResultHandler = ScreenResultHandler(
        backStackEntrySavedStateHandle = backStackEntrySavedStateHandle,
        savedStateHandle = savedStateHandle,
    )

    private val navigationThrottler = Throttler.getNavigationThrottler()

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

    init {
        viewModelScope.launch {
            screenResultHandler.handle<UnscopedDestinations.CitySelector.Result>(
                key = UnscopedDestinations.CitySelector.RESULT_KEY,
            ) { result ->
                val newCity = result.city.toCity()
                val currentCity = city.value
                if (newCity.kladrId != currentCity?.kladrId) {
                    val params = SetUserCityUseCase.Params(newCity)
                    interactor.setUserCity(params)
                        .onFailure {
                            val message = Text.Resource(R.string.city_changing_error)
                            emitSideEffect(SideEffect.ShowToast(message))
                        }
                }
            }
        }
    }

    fun onSignUpClicked() {
        navigationThrottler.throttle {
            val action = ProfileScreenAction.SignUpClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onInfoItemClicked(item: InfoItem) {
        navigationThrottler.throttle {
            when (item) {
                InfoItem.MyOrders -> Unit // TODO: [High] Implement
                InfoItem.City -> {
                    val action = ProfileScreenAction.CityClicked(city.value)
                    emitSideEffect(SideEffect.Navigate(action))
                }

                InfoItem.Shops -> Unit // TODO: [High] Implement
                InfoItem.Help -> {
                    val url = Url(HELP_URL)
                    emitSideEffect(SideEffect.OpenUrl(url))
                }

                InfoItem.AboutCompany -> {
                    val url = Url(ABOUT_COMPANY_URL)
                    emitSideEffect(SideEffect.OpenUrl(url))
                }
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ProfileScreenAction) : SideEffect

        data class OpenUrl(val url: Url) : SideEffect

        data class ShowToast(val message: Text) : SideEffect
    }

    enum class InfoItem { MyOrders, City, Shops, Help, AboutCompany }

    @AssistedFactory
    interface Factory {
        fun create(backStackEntrySavedStateHandle: SavedStateHandle): ProfileViewModel
    }

    companion object {
        private const val HELP_URL = "https://pwa.zarina.ru/help/"
        private const val ABOUT_COMPANY_URL = "https://pwa.zarina.ru/about/"
    }
}
