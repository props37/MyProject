package ru.zarina.zarina.ui.screens.pickup

import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.old.City
import ru.zarina.zarina.domain.old.Offer
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.domain.old.Shop
import ru.zarina.zarina.usecase.old.catalog.GetProductUseCase
import ru.zarina.zarina.usecase.old.shop.GetOffersUseCase
import ru.zarina.zarina.usecase.old.shop.GetStocksUseCase
import ru.zarina.zarina.usecase.old.shop.ReserveUseCase
import ru.zarina.zarina.usecase.old.user.GetCityUseCase
import ru.zarina.zarina.usecase.old.user.ValidateEmailUseCase
import ru.zarina.zarina.usecase.old.user.ValidateNameUseCase
import ru.zarina.zarina.usecase.old.user.ValidatePhoneUseCase
import ru.zarina.zarina.util.base.usecase.invoke

@Factory
class PickupInteractor(
    private val getCityUseCase: GetCityUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val getOffersUseCase: GetOffersUseCase,
    private val getStocksUseCase: GetStocksUseCase,
    private val validateNameUseCase: ValidateNameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePhoneUseCase: ValidatePhoneUseCase,
    private val reserveUseCase: ReserveUseCase,
) {

    suspend fun getCity() = getCityUseCase()

    fun getProduct(id: Product.Id) = getProductUseCase(GetProductUseCase.Params(id))

    suspend fun getOffers(product: Product, city: City) =
        getOffersUseCase(GetOffersUseCase.Params(product, city))

    suspend fun getStocks(offer: Offer, city: City) =
        getStocksUseCase(GetStocksUseCase.Params(offer, city))

    suspend fun validateName(name: String) = validateNameUseCase(ValidateNameUseCase.Params(name))

    suspend fun validateEmail(email: String) =
        validateEmailUseCase(ValidateEmailUseCase.Params(email))

    suspend fun validatePhone(phone: String) =
        validatePhoneUseCase(ValidatePhoneUseCase.Params(phone))

    suspend fun reserve(
        offer: Offer,
        shop: Shop,
        surname: String,
        name: String,
        phone: String,
        email: String,
    ) = reserveUseCase(ReserveUseCase.Params(offer, shop, surname, name, phone, email))

}
