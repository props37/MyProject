package ru.zarina.zarina.data.product

import ru.zarina.zarina.data.product.remote.IProductRemoteSource
import ru.zarina.zarina.domain.Product
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val remote: IProductRemoteSource,
) : IProductRepository {

    override suspend fun getProduct(id: String) = remote.getProduct(id)

    override suspend fun getCompleteLook(product: Product) = remote.getCompleteLook(product)

    override suspend fun getDeliveryAvailability(product: Product) =
        remote.getDeliveryAvailability(product)
}
