package ru.zarina.zarina.usecase.authorization

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.device.DeviceRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.AuthorizationToken
import timber.log.Timber
import javax.inject.Inject

class GetAuthorizationTokenUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val deviceRepository: DeviceRepository,
) : UseCase<Unit, AuthorizationToken>(dispatcher) {
    override suspend fun execute(params: Unit): AuthorizationToken {
        val token = deviceRepository.getToken()
        Timber.v("Device token: $token")
        return token
    }
}
