package ru.livetyping.zarina.usecase.old.shop

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.shop.IShopRepository
import ru.livetyping.zarina.di.old.Qualifiers
import ru.livetyping.zarina.domain.old.Offer
import ru.livetyping.zarina.domain.old.Shop

@Factory
class ReserveUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val shopRepository: IShopRepository,
) : UseCase<ReserveUseCase.Params, Unit>(dispatcher) {
    override suspend fun execute(params: Params) {
        val (offer, shop, firstName, lastName, phone, email) = params

        shopRepository.reserve(offer, shop, firstName, lastName, email, phone)
    }

    data class Params(
        val offer: Offer,
        val shop: Shop,
        val firstName: String,
        val lastName: String,
        val phone: String,
        val email: String,
    )

}
