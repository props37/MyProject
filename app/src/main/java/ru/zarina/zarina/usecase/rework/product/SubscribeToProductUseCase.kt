package ru.zarina.zarina.usecase.rework.product

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.data.rework.product.ProductRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.common.Barcode
import ru.zarina.zarina.usecase.base.UseCase
import javax.inject.Inject

class SubscribeToProductUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val productRepository: ProductRepository,
) : UseCase<SubscribeToProductUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        TODO("Not yet implemented")
    }

    data class Params(
        val barcode: Barcode,
        val firstName: String,
        val email: String,
    )
}
