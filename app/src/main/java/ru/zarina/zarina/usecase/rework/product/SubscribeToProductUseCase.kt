package ru.zarina.zarina.usecase.rework.product

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.data.rework.product.ProductRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.common.Barcode
import ru.zarina.zarina.domain.rework.common.exception.ValidationException
import ru.zarina.zarina.base.usecase.UseCase
import ru.zarina.zarina.usecase.rework.user.ValidateEmailUseCase
import ru.zarina.zarina.usecase.rework.user.ValidateFirstNameUseCase
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
        val email = params.email.trim()

        val firstName = params.name.split(' ').firstOrNull()?.trim().orEmpty()

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
        val name: String,
        val email: String,
    )
}
