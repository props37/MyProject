package ru.livetyping.zarina.feature.home.ui.impl.impl.component

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import ru.livetyping.zarina.feature.home.domain.usecase.GetHomeContentUseCase

internal class HomeContentComponent(
    private val getHomeContentUseCase: GetHomeContentUseCase,
) {
    private val operationTracker = OperationTracker()

    private var contentJob: Job? = null

    private val _contentResult = MutableStateFlow<Result<HomeContent>?>(null)
    val contentResult: StateFlow<Result<HomeContent>?> = _contentResult.asStateFlow()

    val isContentLoading: Flow<Boolean> = operationTracker.isOperationOngoing(ContentRequest.LOADING)

    val isContentRefreshing: Flow<Boolean> = operationTracker.isOperationOngoing(ContentRequest.REFRESHING)

    suspend fun fetchContent(request: ContentRequest) {
        if (contentJob?.isActive == true) return

        coroutineScope {
            contentJob = launch {
                operationTracker.track(request) {
                    _contentResult.value = getHomeContentUseCase()
                }
            }
        }
    }

    enum class ContentRequest : OperationKey { LOADING, REFRESHING }
}
