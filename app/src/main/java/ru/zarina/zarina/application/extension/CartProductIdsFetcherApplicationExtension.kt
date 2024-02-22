package ru.zarina.zarina.application.extension

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.zarina.zarina.application.extension.base.ApplicationExtension
import ru.zarina.zarina.usecase.rework.cart.FetchCartProductIdsUseCase
import ru.zarina.zarina.utils.clean.invoke
import javax.inject.Inject

class CartProductIdsFetcherApplicationExtension @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val fetchCartProductIdsUseCase: FetchCartProductIdsUseCase,
) : ApplicationExtension {
    override fun install(application: Application) {
        coroutineScope.launch {
            fetchCartProductIdsUseCase()
        }
    }
}
