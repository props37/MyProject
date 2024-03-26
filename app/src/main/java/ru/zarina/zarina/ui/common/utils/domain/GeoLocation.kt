package ru.zarina.zarina.ui.common.utils.domain

import com.google.android.gms.maps.model.LatLng
import ru.zarina.zarina.domain.old.GeoLocation

fun GeoLocation.toLatLng(): LatLng = LatLng(latitude, longitude)
