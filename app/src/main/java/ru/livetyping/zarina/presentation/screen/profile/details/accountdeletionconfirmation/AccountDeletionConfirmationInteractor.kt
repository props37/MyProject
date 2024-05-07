package ru.livetyping.zarina.presentation.screen.profile.details.accountdeletionconfirmation

import ru.livetyping.zarina.usecase.user.DeleteAccountUseCase
import javax.inject.Inject

class AccountDeletionConfirmationInteractor @Inject constructor(
    val deleteAccount: DeleteAccountUseCase,
)
