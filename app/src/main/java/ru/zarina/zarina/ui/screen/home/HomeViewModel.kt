package ru.zarina.zarina.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.zarina.zarina.domain.rework.content.HomeBanners
import ru.zarina.zarina.ui.common.base.ErrorStateRework
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.utils.clean.invoke
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val interactor: HomeInteractor,
) : ViewModel(), SideEffectSource<HomeViewModel.SideEffect> by SideEffectSourceImpl() {

    private var fetchHomeBanners: Job? = null

    private val _bannersState = MutableStateFlow<BannersState>(BannersState.Loading)
    val bannersState = _bannersState.asStateFlow()

    init {
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
}
