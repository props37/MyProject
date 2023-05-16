package ru.zarina.zarina.ui.screens.pickup.root.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import ru.zarina.zarina.domain.Stock

@Composable
fun ShopMap(
    stocks: List<Stock>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize()
        )
    }
}
