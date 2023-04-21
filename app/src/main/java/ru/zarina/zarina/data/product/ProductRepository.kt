package ru.zarina.zarina.data.product

import ru.zarina.zarina.data.product.remote.IProductRemoteSource
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val remote: IProductRemoteSource,
) : IProductRepository {

    override suspend fun getProduct(id: String) = remote.getProduct(id)

}
