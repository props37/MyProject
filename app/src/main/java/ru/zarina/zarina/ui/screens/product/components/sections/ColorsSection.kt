package ru.zarina.zarina.ui.screens.product.components.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.toImmutableList
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.ui.common.components.color.ColorPicker

@Composable
fun ColorsSection(
    product: Product,
    onVariantClick: (Product.Variant) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = remember(product) { product.colorVariants.map { it.key }.toImmutableList() }
    val selectedColor = remember(product) {
        product.colorVariants.entries.firstOrNull { it.value.isCurrent }?.key
    }
    ColorPicker(
        colors = colors,
        onColorSelected = { color ->
            val variant = product.colorVariants[color]
            if (variant != null) onVariantClick(variant)
        },
        selectedColor = selectedColor,
        modifier = modifier
    )
}
