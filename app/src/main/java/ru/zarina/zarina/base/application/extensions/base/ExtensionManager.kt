package ru.zarina.zarina.base.application.extensions.base

import org.koin.core.annotation.Factory
import ru.zarina.zarina.base.application.extensions.MindboxExtension
import ru.zarina.zarina.base.application.extensions.TimberExtension

@Factory
class ExtensionManager(
    timber: TimberExtension,
    mindbox: MindboxExtension,
) {

    val extensions = listOf(timber, mindbox)

}
