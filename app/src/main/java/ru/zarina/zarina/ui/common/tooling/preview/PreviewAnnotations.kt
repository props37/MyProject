package ru.zarina.zarina.ui.common.tooling.preview

import androidx.compose.ui.tooling.preview.Preview

private const val FontScaleGroup = "Font scales"
private const val DensityGroup = "Densities"

@Preview(
    name = "Small font",
    group = FontScaleGroup,
    fontScale = 0.5f
)
@Preview(
    name = "Large font",
    group = FontScaleGroup,
    fontScale = 1.5f
)
annotation class FontScalePreviews

// Sample screen sizes taken from https://gist.github.com/uqmessias/4bb9d8ed90d3ebca1c387c114a71c66a
@Preview(
    name = "mdpi / 160",
    group = DensityGroup,
    device = "spec:width=320px,height=480px,dpi=160",
)
@Preview(
    name = "hdpi / 240",
    group = DensityGroup,
    device = "spec:width=480px,height=800px,dpi=240",
)
@Preview(
    name = "xhdpi / 320",
    group = DensityGroup,
    device = "spec:width=720px,height=1280px,dpi=320",
)
@Preview(
    name = "xxhdpi / 480",
    group = DensityGroup,
    device = "spec:width=1080px,height=2340px,dpi=480",
)
@Preview(
    name = "xxxhdpi / 640",
    group = DensityGroup,
    device = "spec:width=1440px,height=2560px,dpi=640",
)
annotation class DensityPreviews
