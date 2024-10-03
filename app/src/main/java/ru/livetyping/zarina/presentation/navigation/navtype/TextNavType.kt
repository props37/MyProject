package ru.livetyping.zarina.presentation.navigation.navtype

import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.navigation.base.ParcelableNavType

val NavType.Companion.TextType: TextNavType
    get() = TextNavType()

class TextNavType : ParcelableNavType<Text?>(
    isNullableAllowed = true,
    serializer = Json.serializersModule.serializer(),
)
