package ru.livetyping.zarina.core.domain.usecase.gender

import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface SetLastContentGenderUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(val gender: Gender)

    public companion object {
        public fun getInstance(
            contentRepository: ContentRepository,
            logger: UseCaseLogger?,
        ): SetLastContentGenderUseCase {
            return SetLastContentGenderUseCaseImpl(
                contentRepository = contentRepository,
                logger = logger,
            )
        }
    }
}
