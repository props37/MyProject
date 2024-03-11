package ru.zarina.zarina.usecase.rework.geography

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.geography.GeographyRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.base.usecase.FlowUseCase
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
