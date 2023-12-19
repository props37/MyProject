package ru.zarina.zarina.base.application.extension.base

import org.koin.core.annotation.Factory
import ru.zarina.zarina.base.application.extension.CoilExtension
import ru.zarina.zarina.base.application.extension.MindboxExtension
import ru.zarina.zarina.base.application.extension.TimberExtension

@Factory
class ApplicationExtensionManager(
    timber: TimberExtension,
    mindbox: MindboxExtension,
    coil: CoilExtension,
) {
    val extensions = listOf(timber, mindbox, coil)
}
