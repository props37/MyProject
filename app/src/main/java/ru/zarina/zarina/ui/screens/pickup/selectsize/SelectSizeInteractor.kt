package ru.zarina.zarina.ui.screens.pickup.selectsize

import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.usecase.old.catalog.GetProductUseCase

@Factory
class SelectSizeInteractor(
    private val getProductUseCase: GetProductUseCase,
) {

    suspend fun getProduct(id: Product.Id) = getProductUseCase(GetProductUseCase.Params(id))

}
