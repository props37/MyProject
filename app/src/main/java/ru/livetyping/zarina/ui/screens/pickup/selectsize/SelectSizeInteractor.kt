package ru.livetyping.zarina.ui.screens.pickup.selectsize

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.domain.old.Product
import ru.livetyping.zarina.usecase.old.catalog.GetProductUseCase

@Factory
class SelectSizeInteractor(
    private val getProductUseCase: GetProductUseCase,
) {

    suspend fun getProduct(id: Product.Id) = getProductUseCase(GetProductUseCase.Params(id))

}
