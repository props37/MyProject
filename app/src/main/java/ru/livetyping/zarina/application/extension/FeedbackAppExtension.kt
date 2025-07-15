package ru.livetyping.zarina.application.extension

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.feedback.impl.FeedbackInitializer
import javax.inject.Inject

class FeedbackAppExtension @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val getUserFlowUseCase: GetUserFlowUseCase,
) : ApplicationExtension {
    override fun install(application: Application) {
        updateUserId()
    }

    private fun updateUserId() {
        getUserFlowUseCase(GetUserFlowUseCase.Params(CachePolicy.LocalOnly))
            .map { result ->
                result.getOrNull()
            }
            .distinctUntilChanged()
            .onEach { user ->
                FeedbackInitializer.updateUserId(user?.id?.value)
            }
            .launchIn(coroutineScope)
    }
}
