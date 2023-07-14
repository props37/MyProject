package ru.zarina.zarina.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.Banner
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue

@KoinViewModel
class HomeViewModel(
    private val interactor: HomeInteractor,
) : ViewModel(),
    ISideEffectSource<HomeViewModel.SideEffect> by SideEffectQueue() {

    private val _banners = MutableStateFlow<Result<List<Banner>>?>(null)
    val banners = _banners
        .map { it?.getOrNull().orEmpty().toPersistentList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), persistentListOf())

    // TODO add error display

    init {
        loadBanners()
    }

    private fun loadBanners() {
        viewModelScope.launch {
            _banners.value = interactor.getBanners()
        }
    }

    fun onBannerClick(banner: Banner) {
        // TODO handle banner click
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
