package ru.zarina.zarina.usecase.shop

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.shop.IShopRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Shop
import javax.inject.Inject

class ReserveUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
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
