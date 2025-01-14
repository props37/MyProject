package ru.livetyping.zarina.core.domain.usecase.cart

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCartProductCountFlowUseCaseImpl(
    private val cartRepository: CartRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Unit, Int>(logger), GetCartProductCountFlowUseCase {

    override fun execute(params: Unit): Flow<Int> {
        return cartRepository.getCartProductCountFlow()
    }

    override fun invoke(): Flow<Result<Int>> {
        return call(Unit)
    }

    private companion object {
        private const val TAG = "GetCartProductCountFlowUseCaseImpl"
    }
}
