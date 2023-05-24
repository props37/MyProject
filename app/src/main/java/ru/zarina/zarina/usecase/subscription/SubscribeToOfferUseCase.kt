package ru.zarina.zarina.usecase.subscription

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.subscription.ISubscriptionRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import javax.inject.Inject

class SubscribeToOfferUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val subscriptionRepository: ISubscriptionRepository,
) : UseCase<SubscribeToOfferUseCase.Params, Unit>(dispatcher) {
    override suspend fun execute(params: Params) {
        val (barcode, name, email) = params

        subscriptionRepository.subscribe(barcode, name, email)
    }

    data class Params(
        val offerBarcode: String,
        val name: String,
        val email: String,
    )

}
