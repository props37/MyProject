package ru.zarina.zarina.usecase.onboarding

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.device.IDeviceRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.usecase.user.SetCityUseCase
import javax.inject.Inject

class FinishOnboardingUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val setCity: SetCityUseCase,
    private val deviceRepository: IDeviceRepository,
) : UseCase<FinishOnboardingUseCase.Params, Unit>(dispatcher) {
    override suspend fun execute(params: Params) {
        val (city) = params

        if (city != null) setCity(city)
        deviceRepository.setIsOnboardingCompleted(true)
    }

    data class Params(
        val selectedCity: City?,
    )

}
