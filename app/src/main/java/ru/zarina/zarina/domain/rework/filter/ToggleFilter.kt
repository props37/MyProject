package ru.zarina.zarina.domain.rework.filter

data class ToggleFilter(
    val isEnabled: Boolean,
    override val type: Filter.Type,
) : Filter {
    override val isEmpty: Boolean get() = !isEnabled
}
