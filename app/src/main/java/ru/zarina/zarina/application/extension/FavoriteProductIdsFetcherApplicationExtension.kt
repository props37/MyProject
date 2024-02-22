package ru.zarina.zarina.application.extension

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.zarina.zarina.application.extension.base.ApplicationExtension
import ru.zarina.zarina.usecase.rework.favorite.FetchFavoriteProductIdsUseCase
import ru.zarina.zarina.utils.clean.invoke
import javax.inject.Inject

class FavoriteProductIdsFetcherApplicationExtension @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val fetchFavoriteProductIdsUseCase: FetchFavoriteProductIdsUseCase,
) : ApplicationExtension {
    override fun install(application: Application) {
        coroutineScope.launch {
            fetchFavoriteProductIdsUseCase()
        }
    }
}
