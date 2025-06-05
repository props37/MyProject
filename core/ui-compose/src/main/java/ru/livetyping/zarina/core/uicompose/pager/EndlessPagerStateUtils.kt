package ru.livetyping.zarina.core.uicompose.pager

public object EndlessPagerStateUtils {
    public fun <T> getLooping(list: List<T>, index: Int): T? {
        return if (list.isEmpty()) {
            null
        } else {
            list.getOrNull(index % list.size)
        }
    }
}
