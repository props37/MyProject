package ru.zarina.zarina.usecase.rework.geography

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.data.rework.geography.GeographyRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.usecase.base.UseCase
import timber.log.Timber
import javax.inject.Inject

class UpdateUserCityUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val geographyRepository: GeographyRepository,
) : UseCase<UpdateUserCityUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val city = params.city
        Timber.v("Update user city: $city")
        geographyRepository.updateUserCity(city)
    }

    data class Params(val city: City)
}
