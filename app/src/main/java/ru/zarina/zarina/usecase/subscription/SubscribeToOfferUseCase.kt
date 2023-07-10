package ru.zarina.zarina.usecase.subscription

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.subscription.ISubscriptionRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Barcode

@Factory
class SubscribeToOfferUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val subscriptionRepository: ISubscriptionRepository,
) : UseCase<SubscribeToOfferUseCase.Params, Unit>(dispatcher) {
    override suspend fun execute(params: Params) {
        val (barcode, name, email) = params

        subscriptionRepository.subscribe(barcode, name, email)
    }

    data class Params(
        val offerBarcode: Barcode,
        val name: String,
        val email: String,
    )

}
