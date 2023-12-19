package ru.zarina.zarina.base.application.extensions.base

import org.koin.core.annotation.Factory
import ru.zarina.zarina.base.application.extensions.CoilExtension
import ru.zarina.zarina.base.application.extensions.MindboxExtension
import ru.zarina.zarina.base.application.extensions.TimberExtension

@Factory
class ApplicationExtensionManager(
    timber: TimberExtension,
    mindbox: MindboxExtension,
    coil: CoilExtension,
) {
    val extensions = listOf(timber, mindbox, coil)
}
