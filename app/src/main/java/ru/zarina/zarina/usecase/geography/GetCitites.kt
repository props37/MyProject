package ru.zarina.zarina.usecase.geography

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.geography.IGeographyRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.City
import javax.inject.Inject

class GetCititesUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val geographyRepository: IGeographyRepository,
) : UseCase<GetCititesUseCase.Params, List<City>>(dispatcher) {

    override suspend fun execute(params: Params): List<City> {
        val searchTerm = params.searchTerm?.trim()?.takeIf { it.isNotBlank() }

        return geographyRepository.getCities(searchTerm)
    }

    data class Params(
        val searchTerm: String?,
    )

}
