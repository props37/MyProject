package ru.livetyping.zarina.usecase.old.subscription

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.subscription.ISubscriptionRepository
import ru.livetyping.zarina.di.old.Qualifiers
import ru.livetyping.zarina.domain.old.Barcode

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
