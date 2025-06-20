package ru.livetyping.zarina.core.uimodel.geo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.geo.Building
import ru.livetyping.zarina.core.domain.model.geo.FiasId

@Serializable
@Parcelize
public data class BuildingParcelable(
    val id: String,
    val name: String,
) : Parcelable {
    public fun toBuilding(): Building {
        return Building(
            id = FiasId(id),
            name = name,
        )
    }

    public companion object {
        public fun from(building: Building): BuildingParcelable {
            return BuildingParcelable(
                id = building.id.value,
                name = building.name,
            )
        }
    }
}
