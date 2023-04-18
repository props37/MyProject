package ru.zarina.zarina.usecase.authorization

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.device.DeviceRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.AuthorizationToken
import javax.inject.Inject

class GetAuthorizationTokenUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val deviceRepository: DeviceRepository,
) : UseCase<Unit, AuthorizationToken>(dispatcher) {
    override suspend fun execute(params: Unit): AuthorizationToken {
        return deviceRepository.getToken()
    }
}
