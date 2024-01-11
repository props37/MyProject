package ru.zarina.zarina.ui.screen.catalog

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.parcelize.Parcelize
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<CatalogViewModel.SideEffect> by SideEffectSourceImpl() {

    val searchQuery = savedStateHandle.getStateFlow(
        key = KEY_SEARCH_QUERY,
        initialValue = "",
    )

    val genderPickerTabs = MutableStateFlow(GenderPickerTab.entries.toList()).asStateFlow()

    val currentGenderPickerTab = savedStateHandle.getStateFlow(
        key = KEY_CURRENT_GENDER_PICKER_TAB,
        initialValue = GenderPickerTab.FOR_WOMEN,
    )

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[KEY_SEARCH_QUERY] = query
    }

    fun onSearchBarClearClicked() {
        savedStateHandle[KEY_SEARCH_QUERY] = ""
    }

    fun onSearchBarCancelClicked() {
        emitSideEffect(SideEffect.FreeSearchBarFocus)
    }

    fun onGenderPickerTabClicked(tab: GenderPickerTab) {
        savedStateHandle[KEY_CURRENT_GENDER_PICKER_TAB] = tab
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data object FreeSearchBarFocus : SideEffect
    }

    @Parcelize
    enum class GenderPickerTab : Parcelable { FOR_WOMEN, FOR_MEN }

    companion object {
        private const val KEY_SEARCH_QUERY = "search_query"
        private const val KEY_CURRENT_GENDER_PICKER_TAB = "current_gender_picker_tab"
    }
}
