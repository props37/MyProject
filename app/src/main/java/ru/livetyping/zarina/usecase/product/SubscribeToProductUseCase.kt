package ru.livetyping.zarina.usecase.product

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.product.ProductRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.usecase.user.ValidateEmailUseCase
import ru.livetyping.zarina.usecase.user.ValidateFirstNameUseCase
import timber.log.Timber
import javax.inject.Inject

class SubscribeToProductUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val productRepository: ProductRepository,
    private val validateFirstNameUseCase: ValidateFirstNameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
) : UseCase<SubscribeToProductUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val barcode = params.barcode
        val firstName = params.firstName.split(' ').firstOrNull()?.trim().orEmpty()
        val email = params.email
        Timber.v("Subscribe to product $barcode. First name: $firstName, email: $email")

        val firstNameValidationException =
            validateFirstNameUseCase(ValidateFirstNameUseCase.Params(firstName)).exceptionOrNull()
        val emailValidationException =
            validateEmailUseCase(ValidateEmailUseCase.Params(email)).exceptionOrNull()

        val validationException = ValidationException.from(
            firstNameValidationException,
            emailValidationException,
        )
        if (validationException != null) throw validationException

        productRepository.subscribeToProduct(barcode, firstName, email)
    }

    data class Params(
        val barcode: Barcode,
        val firstName: String,
        val email: Email,
    )
}
