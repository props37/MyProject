package ru.livetyping.zarina.usecase.geography

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.geography.GeographyRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.geography.City
import javax.inject.Inject

class GetCitiesFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val geographyRepository: GeographyRepository,
) : FlowUseCase<GetCitiesFlowUseCase.Params, List<City>>(dispatcher) {

    override fun execute(params: Params): Flow<List<City>> {
        val nameQuery = if (params.nameQuery.isNullOrBlank()) null else params.nameQuery
        return geographyRepository.getCitiesFlow(nameQuery)
    }

    data class Params(val nameQuery: String?)
}
