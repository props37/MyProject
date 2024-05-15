package ru.livetyping.zarina.domain.filter

data class PickupStoreFilterItem(
    override val id: Id,
    override val name: String,
    override val isSelected: Boolean,
) : ListFilterItem(id, name, isSelected)
