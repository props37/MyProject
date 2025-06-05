package ru.livetyping.zarina.feature.product.ui.impl.impl.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.coroutinesutil.WhileUiSubscribed
import ru.livetyping.zarina.core.domain.analytics.toAppMetricaProduct
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.component.ProductComponent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductState
import javax.inject.Inject

@HiltViewModel
internal class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: ProductDependencies,
) : ViewModel(), SideEffectSource<ProductSideEffect> by SideEffectSourceImpl() {

    private val productComponent = ProductComponent(
        getProductUseCase = deps.getProduct,
        coroutineScope = viewModelScope,
    )

    private val navEntry = savedStateHandle.toRoute<ProductFeature.NavEntry.StartNavEntry>()

    init {
        productComponent.setProductId(navEntry.getProductId())
    }

    private var reportScreenCreatedJob: Job? = null

    private val productStateBuilder = ProductState.Builder()

    val productState: StateFlow<ProductState> = combine(
        productComponent.productResult,
        productComponent.isProductLoading,
    ) { productResult, isProductLoading ->
        productStateBuilder.build(
            productResult = productResult,
            isProductLoading = isProductLoading,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = ProductState.Loading,
    )

    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event) {
            LifecycleEvent.ON_CREATE -> onScreenCreated()
            else -> Unit
        }
    }

    // TODO: [Top] Implement
    fun onProductEvent(event: ProductEvent) {
        when (event) {
            ProductEvent.BackClicked -> TODO()
            ProductEvent.ProductRefreshTriggered -> TODO()
        }
    }

    private fun onScreenCreated() {
        reportScreenCreated()
    }

    private fun reportScreenCreated() {
        deps.appMetrica.reportScreenOpened(Screen.Product)

        reportScreenCreatedJob?.cancel()
        reportScreenCreatedJob = viewModelScope.launch {
            val productResult = productComponent.productResult.firstOrNull { it?.isSuccess == true }
            val product = productResult?.getOrNull()
            if (product != null) {
                deps.appMetrica.reportProductScreenOpened(product.toAppMetricaProduct())
            }
        }
    }
}
