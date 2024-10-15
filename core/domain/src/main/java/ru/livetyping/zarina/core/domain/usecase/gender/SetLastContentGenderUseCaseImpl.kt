package ru.livetyping.zarina.core.domain.usecase.gender

import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.domain.usecase.gender.SetLastContentGenderUseCaseImpl.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class SetLastContentGenderUseCaseImpl(
    private val contentRepository: ContentRepository,
    logger: UseCaseLogger?,
) : SetLastContentGenderUseCase, UseCase<Params, Unit>(logger) {
    override suspend fun execute(params: Params) {
        contentRepository.setLastContentGender(params.gender)
    }

    override suspend fun invoke(gender: Gender) {
        val params = Params(gender)
        execute(params)
    }

    data class Params(val gender: Gender)
}
