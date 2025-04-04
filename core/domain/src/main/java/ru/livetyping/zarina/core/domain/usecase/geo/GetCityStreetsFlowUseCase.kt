package ru.livetyping.zarina.core.domain.usecase.geo

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.geo.Street
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetCityStreetsFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<List<Street>>>

    public data class Params(
        val nameQuery: String,
        val cityKladrId: KladrId? = null,
    )

    public companion object {
        public fun getInstance(
            geographyRepository: GeographyRepository,
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): GetCityStreetsFlowUseCase {
            return GetCityStreetsFlowUseCaseImpl(
                geographyRepository = geographyRepository,
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
