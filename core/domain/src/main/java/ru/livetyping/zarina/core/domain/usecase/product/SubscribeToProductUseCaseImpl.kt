package ru.livetyping.zarina.core.domain.usecase.product

import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.user.exception.EmailException
import ru.livetyping.zarina.core.domain.model.user.exception.FirstNameException
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.domain.usecase.product.SubscribeToProductUseCase.Params
import ru.livetyping.zarina.core.domain.validation.EmailValidator
import ru.livetyping.zarina.core.domain.validation.FirstNameValidator
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class SubscribeToProductUseCaseImpl(
    private val productRepository: ProductRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), SubscribeToProductUseCase {

    override suspend fun execute(params: Params) {
        val firstName = params.name.trim().split(' ').firstOrNull().orEmpty()
        val email = params.email

        validate(firstName, email)

        productRepository.subscribeToProduct(params.barcode, firstName, email)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private fun validate(firstName: String, email: Email) {
        val firstNameException = try {
            FirstNameValidator().validate(firstName)
            null
        } catch (e: FirstNameException) {
            e
        }
        val emailException = try {
            EmailValidator().validate(email)
            null
        } catch (e: EmailException) {
            e
        }

        val exceptions = listOfNotNull(firstNameException, emailException)
        when {
            exceptions.size == 1 -> throw exceptions.first()
            exceptions.size > 1 -> throw CombinedValidationException(exceptions)
        }
    }

    private companion object {
        private const val TAG = "SubscribeToProductUseCaseImpl"
    }
}
