package ru.livetyping.zarina.core.domain.usecase.gender

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public class GetLastContentGenderFlowUseCase(
    private val contentRepository: ContentRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Unit, Gender>(logger) {

    override fun execute(params: Unit): Flow<Gender> {
        return contentRepository.getLastContentGenderFlow()
            .map { gender -> gender ?: Gender.getDefault() }
    }
}
