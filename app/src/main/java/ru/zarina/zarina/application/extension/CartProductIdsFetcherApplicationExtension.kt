package ru.zarina.zarina.application.extension

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.zarina.zarina.application.extension.base.ApplicationExtension
import ru.zarina.zarina.usecase.rework.authorization.GetAuthorizationTokensFlowUseCase
import ru.zarina.zarina.usecase.rework.cart.FetchCartProductIdsUseCase
import ru.zarina.zarina.utils.clean.invoke
import javax.inject.Inject

class CartProductIdsFetcherApplicationExtension @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val getAuthorizationTokensFlowUseCase: GetAuthorizationTokensFlowUseCase,
    private val fetchCartProductIdsUseCase: FetchCartProductIdsUseCase,
) : ApplicationExtension {

    override fun install(application: Application) {
        getAuthorizationTokensFlowUseCase()
            .map { it.getOrNull() }
            .distinctUntilChanged()
            .onEach {
                if (it != null) {
                    fetchCartProductIdsUseCase()
                }
            }
            .launchIn(coroutineScope)
    }
}
