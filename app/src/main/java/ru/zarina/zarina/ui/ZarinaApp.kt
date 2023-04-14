package ru.zarina.zarina.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ZarinaApp() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Zarina")
    }
}
