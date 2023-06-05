package ru.zarina.zarina.usecase.catalog

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.product.IProductRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.ProductSort
import javax.inject.Inject

class GetProductsPageUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val productRepository: IProductRepository,
) : UseCase<GetProductsPageUseCase.Params, Page<List<Product>>>(dispatcher) {
    override suspend fun execute(params: Params): Page<List<Product>> {
        val (category, sort, pageIndex) = params
        return productRepository.getProducts(category, sort, pageIndex)
    }

    data class Params(
        val category: Category,
        val sort: ProductSort,
        val pageIndex: Int,
    )

}
