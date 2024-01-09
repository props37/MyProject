package ru.zarina.zarina.ui.screen.home

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import ru.zarina.zarina.domain.rework.content.HomeBanners
import ru.zarina.zarina.ui.common.base.ErrorStateRework
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.utils.clean.invoke
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: HomeInteractor,
) : ViewModel(), SideEffectSource<HomeViewModel.SideEffect> by SideEffectSourceImpl() {

    private var fetchHomeBanners: Job? = null

    val tabs = MutableStateFlow(Tab.entries.toList()).asStateFlow()

    // TODO: [Low] Store the last selected tab on the disk
    val currentTab = savedStateHandle.getStateFlow(
        key = KEY_CURRENT_TAB,
        initialValue = Tab.FOR_WOMEN,
    )

    private val _bannersState = MutableStateFlow<BannersState>(BannersState.Loading)
    val bannersState = _bannersState.asStateFlow()

    init {
        fetchBanners()
    }

    fun onTabClicked(tab: Tab) {
        savedStateHandle[KEY_CURRENT_TAB] = tab
    }

    fun onBannersErrorRefreshClicked() {
        _bannersState.value = BannersState.Loading
        fetchBanners()
    }

    private fun fetchBanners() {
        fetchHomeBanners?.cancel()
        fetchHomeBanners = viewModelScope.launch {
            interactor.getHomeBanners().collect { result ->
                val bannersState = result.fold(
                    onSuccess = { banners ->
                        BannersState.Success(banners)
                    },
                    onFailure = { throwable ->
                        val errorState = when (throwable) {
                            is IOException -> ErrorStateRework.NETWORK
                            else -> ErrorStateRework.GENERIC
                        }
                        BannersState.Error(errorState)
                    },
                )
                _bannersState.value = bannersState
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect

    sealed class BannersState {
        data object Loading : BannersState()

        data class Success(val banners: HomeBanners) : BannersState()

        data class Error(val errorState: ErrorStateRework) : BannersState()
    }

    @Parcelize
    enum class Tab : Parcelable { FOR_WOMEN, FOR_MEN }

    companion object {
        private const val KEY_CURRENT_TAB = "current_tab"
    }
}
