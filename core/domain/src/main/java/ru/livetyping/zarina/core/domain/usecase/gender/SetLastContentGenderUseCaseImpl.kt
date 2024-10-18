package ru.livetyping.zarina.core.domain.usecase.gender

import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.domain.usecase.gender.SetLastContentGenderUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class SetLastContentGenderUseCaseImpl(
    private val contentRepository: ContentRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), SetLastContentGenderUseCase {

    override suspend fun execute(params: Params) {
        contentRepository.setLastContentGender(params.gender)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }
}
