package ru.livetyping.zarina.usecase.old.authorization

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.device.IDeviceRepository
import ru.livetyping.zarina.di.old.Qualifiers
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
