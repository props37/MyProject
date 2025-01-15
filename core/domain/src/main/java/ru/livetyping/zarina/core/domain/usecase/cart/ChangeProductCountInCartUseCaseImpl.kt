package ru.livetyping.zarina.core.domain.usecase.cart

import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.usecase.cart.ChangeProductCountInCartUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class ChangeProductCountInCartUseCaseImpl(
    private val cartRepository: CartRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), ChangeProductCountInCartUseCase {

    override suspend fun execute(params: Params) {
        cartRepository.changeProductCount(
            barcode = params.barcode,
            count = params.count,
            cartType = params.cartType,
        )
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "ChangeProductCountInCartUseCaseImpl"
    }
}
