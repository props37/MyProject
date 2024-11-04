package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.SetLocalUserCityUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class SetLocalUserCityUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), SetLocalUserCityUseCase {

    override suspend fun execute(params: Params) {
        userRepository.setLocalUserCity(params.city)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }
}
