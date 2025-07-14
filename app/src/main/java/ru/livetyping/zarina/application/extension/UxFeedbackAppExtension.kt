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
import ru.uxfeedback.pub.sdk.UxFeedback
import javax.inject.Inject

class UxFeedbackAppExtension @Inject constructor(
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
                val userId = user?.id
                if (userId != null) {
                    UxFeedback.sdk?.properties?.put(USER_ID_KEY, userId.value)
                } else {
                    UxFeedback.sdk?.properties?.remove(USER_ID_KEY)
                }
            }
            .launchIn(coroutineScope)
    }

    private companion object {
        private const val USER_ID_KEY = "userId"
    }
}
