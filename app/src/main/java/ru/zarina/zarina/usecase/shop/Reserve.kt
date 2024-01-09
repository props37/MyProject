package ru.zarina.zarina.usecase.shop

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.shop.IShopRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Shop
import ru.zarina.zarina.usecase.base.UseCase

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
