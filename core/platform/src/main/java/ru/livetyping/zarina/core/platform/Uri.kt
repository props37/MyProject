package ru.livetyping.zarina.core.platform

import android.net.Uri
import ru.livetyping.zarina.core.domain.model.common.Url

public fun Url.toUri(): Uri = Uri.parse(this.value)
