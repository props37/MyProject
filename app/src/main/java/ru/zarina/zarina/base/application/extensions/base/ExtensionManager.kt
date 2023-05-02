package ru.zarina.zarina.base.application.extensions.base

import ru.zarina.zarina.base.application.extensions.MindboxExtension
import ru.zarina.zarina.base.application.extensions.TimberExtension
import javax.inject.Inject

class ExtensionManager @Inject constructor(
    timber: TimberExtension,
    mindbox: MindboxExtension,
) {

    val extensions = listOf(timber, mindbox)

}
