package ru.zarina.zarina.application.extension

import org.koin.core.annotation.Factory

@Factory
class ApplicationExtensionManager(
    timber: TimberExtension,
    mindbox: MindboxExtension,
    coil: CoilExtension,
) {
    val extensions = listOf(timber, mindbox, coil)
}
