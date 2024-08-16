package ru.livetyping.zarina.usecase.geography

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.geography.GeographyRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.exception.EmptySearchQueryException
import ru.livetyping.zarina.domain.geography.Building
import ru.livetyping.zarina.domain.geography.KladrId
import javax.inject.Inject

class GetStreetBuildingsFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val geographyRepository: GeographyRepository,
) : FlowUseCase<GetStreetBuildingsFlowUseCase.Params, List<Building>>(dispatcher) {

    override fun execute(params: Params): Flow<List<Building>> {
        if (params.nameQuery.isBlank()) {
            return flow { throw EmptySearchQueryException() }
        }

        return geographyRepository.getStreetBuildings(params.streetKladrId, params.nameQuery)
    }

    data class Params(
        val streetKladrId: KladrId,
        val nameQuery: String,
    )
}
