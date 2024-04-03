package ru.livetyping.zarina.domain.filter

data class ToggleFilter(
    val isEnabled: Boolean,
    override val type: Filter.Type,
) : Filter {
    override val isEmpty: Boolean get() = !isEnabled
}
