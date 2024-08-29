package ru.livetyping.zarina.presentation.model.geography

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.geography.Building
import ru.livetyping.zarina.domain.geography.KladrId

@Serializable
@Parcelize
data class BuildingParcelable(
    val id: String,
    val name: String,
) : Parcelable {
    fun toBuilding(): Building {
        return Building(
            id = KladrId(id),
            name = name,
        )
    }

    companion object {
        fun from(building: Building): BuildingParcelable {
            return BuildingParcelable(
                id = building.id.value,
                name = building.name,
            )
        }
    }
}
