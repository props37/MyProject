package ru.zarina.zarina.usecase.authorization

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.device.IDeviceRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.AuthorizationToken
import ru.zarina.zarina.base.usecase.UseCase
import timber.log.Timber

@Factory
class GetAuthorizationTokenUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val deviceRepository: IDeviceRepository,
) : UseCase<Unit, AuthorizationToken>(dispatcher) {
    override suspend fun execute(params: Unit): AuthorizationToken {
        val token = deviceRepository.getToken()
        Timber.v("Device token: $token")
        return token
    }
}
