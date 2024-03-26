package ru.zarina.zarina.ui.common.tooling.preview.parameterprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.ui.common.tooling.FakeDataGenerator

class ProductPreviewParameterProvider : PreviewParameterProvider<Product> {
    override val values: Sequence<Product>
        get() = FakeDataGenerator.getProducts().asSequence()
}
