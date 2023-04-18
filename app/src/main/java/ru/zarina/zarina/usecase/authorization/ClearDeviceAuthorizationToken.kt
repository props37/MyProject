package ru.zarina.zarina.usecase.authorization

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.device.DeviceRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import javax.inject.Inject

class ClearDeviceAuthorizationTokenUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val deviceRepository: DeviceRepository,
) : UseCase<Unit, Unit>(dispatcher) {
    override suspend fun execute(params: Unit) {
        deviceRepository.setToken(null)
    }
}
