package ru.livetyping.zarina.util.compose.text

data class TextTransformation(
    val formatted: String?,
    val originalToTransformed: List<Int>,
    val transformedToOriginal: List<Int>,
)
