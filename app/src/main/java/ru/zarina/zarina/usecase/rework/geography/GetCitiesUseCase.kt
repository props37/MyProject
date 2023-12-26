package ru.zarina.zarina.usecase.rework.geography

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.base.clean.FlowUseCase
import ru.zarina.zarina.data.rework.geography.GeographyRepository
import ru.zarina.zarina.di.reworked.Qualifiers
import ru.zarina.zarina.domain.rework.geography.City
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val geographyRepository: GeographyRepository,
) : FlowUseCase<GetCitiesUseCase.Params, List<City>>(dispatcher) {

    override fun execute(params: Params): Flow<List<City>> {
        val nameQuery = if (params.nameQuery.isNullOrBlank()) null else params.nameQuery
        return geographyRepository.getCities(nameQuery)
    }

    data class Params(val nameQuery: String?)
}
