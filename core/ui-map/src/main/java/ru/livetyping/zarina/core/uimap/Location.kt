package ru.livetyping.zarina.core.uimap

import com.google.android.gms.maps.model.LatLng
import ru.livetyping.zarina.core.domain.model.common.Location

public fun Location.toLatLng(): LatLng = LatLng(latitude, longitude)
