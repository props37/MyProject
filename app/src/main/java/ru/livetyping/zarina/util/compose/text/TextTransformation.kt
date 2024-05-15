package ru.livetyping.zarina.util.compose.text

data class TextTransformation(
    val transformed: String?,
    val originalToTransformed: List<Int>,
    val transformedToOriginal: List<Int>,
)
