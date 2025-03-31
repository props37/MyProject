package ru.livetyping.zarina.core.uicomponent.location

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.usecase.location.GetCurrentLocationFlowUseCase

public class CurrentLocationComponent(
    coroutineScope: CoroutineScope,
    private val getCurrentLocationFlowUseCase: GetCurrentLocationFlowUseCase,
) {
    private val currentLocationRequester = FlowRequester(LocationRequest) {
        getCurrentLocationFlowUseCase()
    }

    public val currentLocation: StateFlow<Location?> = currentLocationRequester.flow
        .map { it.getOrNull() }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
            initialValue = null,
        )

    public fun refreshCurrentLocation() {
        currentLocationRequester.request(LocationRequest)
    }

    private data object LocationRequest : FlowRequest
}
