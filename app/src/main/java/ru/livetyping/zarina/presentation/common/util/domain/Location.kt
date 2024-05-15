package ru.livetyping.zarina.presentation.common.util.domain

import com.google.android.gms.maps.model.LatLng
import ru.livetyping.zarina.domain.location.Location

fun Location.toLatLng(): LatLng = LatLng(this.latitude, this.longitude)
