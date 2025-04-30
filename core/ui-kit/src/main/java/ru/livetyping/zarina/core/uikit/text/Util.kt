package ru.livetyping.zarina.core.uikit.text

import kotlin.text.Typography.nbsp

public fun String.withZarinaBrackets(
    padding: String = PADDING,
): String = "$ZARINA_BRACKET_START$padding$this$padding$ZARINA_BRACKET_END"

public const val ZARINA_BRACKET_START: Char = '['
public const val ZARINA_BRACKET_END: Char = ']'

private const val PADDING = "$nbsp$nbsp"
