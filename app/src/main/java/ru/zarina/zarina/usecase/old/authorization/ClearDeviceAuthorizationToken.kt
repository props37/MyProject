package ru.zarina.zarina.usecase.old.authorization

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.old.device.IDeviceRepository
import ru.zarina.zarina.di.old.Qualifiers
import ru.zarina.zarina.base.usecase.UseCase
import timber.log.Timber

@Factory
class ClearDeviceAuthorizationTokenUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val deviceRepository: IDeviceRepository,
) : UseCase<Unit, Unit>(dispatcher) {
    override suspend fun execute(params: Unit) {
        deviceRepository.setToken(null)
        Timber.v("Cleared device authorization token")
    }
}
