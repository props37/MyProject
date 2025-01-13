package ru.livetyping.zarina.core.platform.settings

import ru.livetyping.zarina.core.platform.PackageName

public sealed class SystemSettings {
    public data class ApplicationDetails(val packageName: PackageName) : SystemSettings()
}
