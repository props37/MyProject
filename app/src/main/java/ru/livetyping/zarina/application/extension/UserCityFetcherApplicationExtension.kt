package ru.livetyping.zarina.application.extension

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import ru.livetyping.zarina.usecase.user.FetchUserCityUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import javax.inject.Inject

class UserCityFetcherApplicationExtension @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val fetchUserCityUseCase: FetchUserCityUseCase,
) : ApplicationExtension {

    override fun install(application: Application) {
        coroutineScope.launch {
            fetchUserCityUseCase()
        }
    }
}
