package ru.livetyping.zarina.application.extension

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import javax.inject.Inject

class UserFetcherApplicationExtension @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val getUserFlow: GetUserFlowUseCase,
) : ApplicationExtension {

    override fun install(application: Application) {
        coroutineScope.launch {
            val params = GetUserFlowUseCase.Params(CachePolicy.Remote())
            getUserFlow(params).firstOrNull()
        }
    }
}
