package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import timber.log.Timber
import javax.inject.Inject

class UpdateUserNotificationSettingsUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : UseCase<UpdateUserNotificationSettingsUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val receiveSms = params.receiveSms
        val receiveEmails = params.receiveEmails
        Timber.v(
            "Update user notification settings. " +
                    "Receive SMS: $receiveSms, receive emails: $receiveEmails"
        )
        userRepository.updateUserNotificationSettings(receiveSms, receiveEmails)
    }

    data class Params(
        val receiveSms: Boolean,
        val receiveEmails: Boolean,
    )
}
