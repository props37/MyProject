package ru.zarina.zarina.application.extension

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.zarina.zarina.application.extension.base.ApplicationExtension
import ru.zarina.zarina.usecase.rework.authorization.GetAuthorizationTokensFlowUseCase
import ru.zarina.zarina.usecase.rework.favorite.FetchFavoriteProductIdsUseCase
import ru.zarina.zarina.util.base.usecase.invoke
import javax.inject.Inject

class FavoriteProductIdsFetcherApplicationExtension @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val getAuthorizationTokensFlowUseCase: GetAuthorizationTokensFlowUseCase,
    private val fetchFavoriteProductIdsUseCase: FetchFavoriteProductIdsUseCase,
) : ApplicationExtension {

    override fun install(application: Application) {
        getAuthorizationTokensFlowUseCase()
            .map { it.getOrNull() }
            .distinctUntilChanged()
            .onEach {
                if (it != null) {
                    fetchFavoriteProductIdsUseCase()
                }
            }
            .launchIn(coroutineScope)
    }
}
