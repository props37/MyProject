package ru.livetyping.zarina.ui.common.utils.domain

import com.google.android.gms.maps.model.LatLng
import ru.livetyping.zarina.domain.old.GeoLocation

fun GeoLocation.toLatLng(): LatLng = LatLng(latitude, longitude)
