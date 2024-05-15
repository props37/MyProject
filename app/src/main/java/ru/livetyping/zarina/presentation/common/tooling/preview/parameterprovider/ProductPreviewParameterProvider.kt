package ru.livetyping.zarina.presentation.common.tooling.preview.parameterprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.presentation.common.tooling.FakeDataGenerator

class ProductPreviewParameterProvider : PreviewParameterProvider<Product> {
    override val values: Sequence<Product>
        get() = FakeDataGenerator.getProductItems().asSequence()
}
