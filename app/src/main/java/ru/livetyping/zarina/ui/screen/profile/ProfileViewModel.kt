package ru.livetyping.zarina.ui.screen.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.user.LoyaltyCard
import ru.livetyping.zarina.domain.user.User
import ru.livetyping.zarina.ui.base.text.Text
import ru.livetyping.zarina.ui.common.screenresult.ScreenResultHandler
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.ui.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.ui.screen.profile.ProfileViewModel.SideEffect
import ru.livetyping.zarina.usecase.user.SetUserCityUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed

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

    private var fetchLoyaltyCardJob: Job? = null

    val user: StateFlow<User?> = interactor.getUserFlow()
        .map { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

    val loyaltyCard: StateFlow<LoyaltyCard?> = interactor.getLoyaltyCardFlow()
        .map { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

    val infoItems: StateFlow<ImmutableList<InfoItem>> = user
        .map { user ->
            getInfoItems(isUserAuthorized = user != null).toImmutableList()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = getInfoItems(isUserAuthorized = false).toImmutableList(),
        )

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
        handleCitySelectorResult()
    }

    fun onScreenOpened() {
        fetchLoyaltyCard()
    }

    fun onProfileDetailsClicked() {
        navigationThrottler.throttle {
            val action = ProfileScreenAction.ProfileDetailsClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onSignInClicked() {
        navigationThrottler.throttle {
            val action = ProfileScreenAction.SignInClicked
            emitSideEffect(SideEffect.Navigate(action))
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
                InfoItem.MyOrders -> {
                    val action = ProfileScreenAction.MyOrdersClicked
                    emitSideEffect(SideEffect.Navigate(action))
                }

                InfoItem.City -> {
                    val action = ProfileScreenAction.CityClicked(city.value)
                    emitSideEffect(SideEffect.Navigate(action))
                }

                InfoItem.Shops -> {
                    val action = ProfileScreenAction.ShopsClicked
                    emitSideEffect(SideEffect.Navigate(action))
                }

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

    private fun fetchLoyaltyCard() {
        if (fetchLoyaltyCardJob?.isActive == true) return
        fetchLoyaltyCardJob = viewModelScope.launch {
            interactor.fetchLoyaltyCard()
        }
    }

    private fun getInfoItems(isUserAuthorized: Boolean): List<InfoItem> {
        return if (isUserAuthorized) {
            InfoItem.entries
        } else {
            InfoItem.entries.filter { it != InfoItem.MyOrders }
        }
    }

    private fun handleCitySelectorResult() {
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
                            val text = Text.Resource(R.string.city_changing_error)
                            val message = ZarinaToastMessage.error(text)
                            emitSideEffect(SideEffect.ShowZarinaToast(message))
                        }
                }
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ProfileScreenAction) : SideEffect

        data class OpenUrl(val url: Url) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    enum class InfoItem { MyOrders, City, Shops, Help, AboutCompany }

    @AssistedFactory
    interface Factory {
        fun create(backStackEntrySavedStateHandle: SavedStateHandle): ProfileViewModel
    }

    companion object {
        private const val HELP_URL = "https://zarina.ru/help/"
        private const val ABOUT_COMPANY_URL = "https://zarina.ru/about/"
    }
}
