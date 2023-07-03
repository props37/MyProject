package ru.zarina.zarina.usecase.onboarding

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.device.IDeviceRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.usecase.user.SetCityUseCase

@Factory
class FinishOnboardingUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val setCity: SetCityUseCase,
    private val deviceRepository: IDeviceRepository,
) : UseCase<FinishOnboardingUseCase.Params, Unit>(dispatcher) {
    override suspend fun execute(params: Params) {
        val (city) = params

        if (city != null) setCity(city).getOrThrow()
        deviceRepository.setIsOnboardingCompleted(true)
    }

    data class Params(
        val selectedCity: City?,
    )

}
