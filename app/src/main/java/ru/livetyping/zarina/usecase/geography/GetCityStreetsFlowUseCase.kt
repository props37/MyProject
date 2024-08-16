package ru.livetyping.zarina.usecase.geography

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.geography.GeographyRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.exception.EmptySearchQueryException
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.geography.Street
import javax.inject.Inject

class GetCityStreetsFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val geographyRepository: GeographyRepository,
) : FlowUseCase<GetCityStreetsFlowUseCase.Params, List<Street>>(dispatcher) {

    override fun execute(params: Params): Flow<List<Street>> {
        if (params.nameQuery.isBlank()) {
            return flow { throw EmptySearchQueryException() }
        }

        return geographyRepository.getCityStreetsFlow(params.cityKladrId, params.nameQuery)
    }

    data class Params(
        val cityKladrId: KladrId,
        val nameQuery: String,
    )
}
