package ru.livetyping.zarina.core.domain.usecase.gender

import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.domain.usecase.gender.SetLastContentGenderUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public class SetLastContentGenderUseCase(
    private val contentRepository: ContentRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger) {

    override suspend fun execute(params: Params) {
        contentRepository.setLastContentGender(params.gender)
    }

    public data class Params(val gender: Gender)
}
