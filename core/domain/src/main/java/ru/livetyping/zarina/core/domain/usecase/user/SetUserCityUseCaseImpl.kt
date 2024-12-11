package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.SetUserCityUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class SetUserCityUseCaseImpl(
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), SetUserCityUseCase {

    override suspend fun execute(params: Params) {
        userRepository.setUserCity(params.city)
        // Fetch cart product IDs because the cart state depends on the location
        cartRepository.getCartProductIdsFlow(CachePolicy.Remote())
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }
}
