package ru.livetyping.zarina.core.uikit.text

import kotlin.text.Typography.nbsp

public fun String.withZarinaBrackets(
    padding: String = PADDING,
): String = "[$padding$this$padding]"

private const val PADDING = "$nbsp$nbsp"
