package ru.livetyping.zarina.feature.cityselector.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.core.navigation.ScreenResult
import ru.livetyping.zarina.core.uimodel.geo.CityParcelable
import java.util.UUID

@Parcelize
public data class CitySelectorResult(
    override val id: String = UUID.randomUUID().toString(),
    val city: CityParcelable,
) : ScreenResult, Parcelable {
    public companion object {
        public const val KEY: String = "city_selector_result_key"
    }
}
