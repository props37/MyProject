package ru.livetyping.zarina.application.extension

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import javax.inject.Inject

class UserCityFetcherApplicationExtension @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val getUserCityFlow: GetUserCityFlowUseCase,
) : ApplicationExtension {

    override fun install(application: Application) {
        coroutineScope.launch {
            val params = GetUserCityFlowUseCase.Params(CachePolicy.Remote())
            getUserCityFlow(params).firstOrNull()
        }
    }
}
