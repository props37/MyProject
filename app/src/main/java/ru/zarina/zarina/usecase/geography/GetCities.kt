package ru.zarina.zarina.usecase.geography

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.geography.IGeographyRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.City
import timber.log.Timber
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val geographyRepository: IGeographyRepository,
) : UseCase<GetCitiesUseCase.Params, List<City>>(dispatcher) {

    override suspend fun execute(params: Params): List<City> {
        val query = params.query?.trim()?.takeIf { it.isNotBlank() }

        val cities = geographyRepository.getCities(query)
        Timber.v("Found ${cities.size} cities with query \"$query\"")
        return cities
    }

    data class Params(
        val query: String?,
    )

}
