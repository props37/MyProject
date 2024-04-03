package ru.livetyping.zarina.util.domain.common

import android.net.Uri
import androidx.core.net.toUri
import ru.livetyping.zarina.domain.common.Url

fun Url.toUri(): Uri = this.value.toUri()
