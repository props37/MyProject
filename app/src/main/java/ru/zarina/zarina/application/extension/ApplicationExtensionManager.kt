package ru.zarina.zarina.application.extension

import javax.inject.Inject

class ApplicationExtensionManager @Inject constructor(
    timber: TimberExtension,
    mindbox: MindboxExtension,
    coil: CoilExtension,
) {
    val extensions = listOf(timber, mindbox, coil)
}
