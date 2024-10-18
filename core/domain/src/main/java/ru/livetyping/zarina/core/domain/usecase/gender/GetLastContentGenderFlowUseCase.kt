package ru.livetyping.zarina.core.domain.usecase.gender

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetLastContentGenderFlowUseCase {
    public operator fun invoke(): Flow<Result<Gender>>

    public companion object {
        public fun getInstance(
            contentRepository: ContentRepository,
            logger: UseCaseLogger?,
        ): GetLastContentGenderFlowUseCase {
            return GetLastContentGenderFlowUseCaseImpl(
                contentRepository = contentRepository,
                logger = logger,
            )
        }
    }
}
