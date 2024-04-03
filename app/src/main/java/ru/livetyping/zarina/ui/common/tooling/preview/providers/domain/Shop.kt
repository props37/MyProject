package ru.livetyping.zarina.ui.common.tooling.preview.providers.domain

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.livetyping.zarina.domain.old.GeoLocation
import ru.livetyping.zarina.domain.old.Shop
import java.util.UUID

class ShopProvider : PreviewParameterProvider<Shop> {
    override val values = sequenceOf(
        Shop(
            id = UUID.randomUUID().toString(),
            name = "ТРЦ Галерея",
            geoLocation = GeoLocation(latitude = 55.043951, longitude = 82.92244),
            address = "ул. Гоголя, 13",
            phone = "8 (383) 230-35-98",
            schedule = "10:00-22:00"
        )
    )
}
