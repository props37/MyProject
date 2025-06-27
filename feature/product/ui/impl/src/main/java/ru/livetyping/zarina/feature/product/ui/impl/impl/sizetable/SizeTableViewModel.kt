package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.WhileUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.ProductMeasurementsState
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.SizeTableEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.SizeTableState
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.ViewMode
import javax.inject.Inject

@HiltViewModel
internal class SizeTableViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SizeTableSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<SizeTableNavEntry>(
        typeMap = SizeTableNavEntry.typeMap(),
    )
    private val productMeasurements = navEntry.getProductMeasurements()
    private val modelInfo = navEntry.getModelInfo()
    private val sizeGuide = navEntry.getSizeGuide().let {
        it.copy(entries = it.entries.sortedBy { it.sizeRu.size })
    }
    private val gender = navEntry.getGender()

    private val viewModes = ViewMode.entries.toImmutableList()
    private val currentViewMode = MutableStateFlow(ViewMode.PRODUCT_MEASUREMENTS)
    private val viewModeSelectorState = currentViewMode.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        transform = { TabRowState(viewModes, it) },
    )

    private val productMeasurementsSizes = productMeasurements.entries
        .mapTo(mutableSetOf()) { it.size }
        .toImmutableList()

    private val selectedProductMeasurementsSize =
        MutableStateFlow(productMeasurementsSizes.firstOrNull())

    private val productMeasurementsHeights = productMeasurements.entries
        .mapTo(mutableSetOf()) { it.height }
        .takeIf { it.size > 1 }
        ?.toImmutableList()

    private val selectedProductMeasurementsHeight =
        MutableStateFlow(productMeasurementsHeights?.firstOrNull())

    private val initialProductMeasurementsState = ProductMeasurementsState(
        sizes = productMeasurementsSizes,
        selectedSize = selectedProductMeasurementsSize.value,
        heights = productMeasurementsHeights,
        selectedHeight = selectedProductMeasurementsHeight.value,
        measurements = persistentListOf(),
        modelInfo = modelInfo,
    )

    private val productMeasurementsState = combine(
        selectedProductMeasurementsSize,
        selectedProductMeasurementsHeight,
    ) { selectedSize, selectedHeight ->
        val measurements = productMeasurements.entries.find { entry ->
            if (selectedHeight != null) {
                entry.size == selectedSize && entry.height == selectedHeight
            } else {
                entry.size == selectedSize
            }
        }?.measurements

        ProductMeasurementsState(
            sizes = productMeasurementsSizes,
            selectedSize = selectedSize,
            heights = productMeasurementsHeights,
            selectedHeight = selectedHeight,
            measurements = measurements?.toImmutableList(),
            modelInfo = modelInfo,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = initialProductMeasurementsState,
    )

    private val initialSizeTableState = SizeTableState(
        viewModeSelectorState = viewModeSelectorState.value,
        sizeGuide = sizeGuide,
        gender = gender,
        productMeasurementsState = initialProductMeasurementsState,
    )

    val sizeTableState: StateFlow<SizeTableState> = combine(
        viewModeSelectorState,
        productMeasurementsState,
    ) { viewModeSelectorState, productMeasurementsState ->
        SizeTableState(
            viewModeSelectorState = viewModeSelectorState,
            productMeasurementsState = productMeasurementsState,
            sizeGuide = sizeGuide,
            gender = gender,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = initialSizeTableState,
    )

    fun onSizeTableEvent(event: SizeTableEvent) {
        when (event) {
            SizeTableEvent.CloseClicked -> onCloseClicked()
            is SizeTableEvent.ViewModeSelected -> onViewModeSelected(event)
            is SizeTableEvent.ProductMeasurementsSizeSelected -> {
                onProductMeasurementsSizeSelected(event)
            }

            is SizeTableEvent.ProductMeasurementsHeightSelected -> {
                onProductMeasurementsHeightSelected(event)
            }
        }
    }

    private fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = SizeTableScreenAction.CloseClicked
            emitSideEffect(SizeTableSideEffect.Navigate(action))
        }
    }

    private fun onViewModeSelected(event: SizeTableEvent.ViewModeSelected) {
        currentViewMode.value = event.mode
    }

    private fun onProductMeasurementsSizeSelected(event: SizeTableEvent.ProductMeasurementsSizeSelected) {
        selectedProductMeasurementsSize.value = event.size
    }

    private fun onProductMeasurementsHeightSelected(event: SizeTableEvent.ProductMeasurementsHeightSelected) {
        selectedProductMeasurementsHeight.value = event.height
    }
}
