package ru.zarina.zarina.base.application.extensions.base

import org.koin.core.annotation.Factory
import ru.zarina.zarina.base.application.extensions.MindboxExtension
import ru.zarina.zarina.base.application.extensions.TimberExtension
import javax.inject.Inject

@Factory
class ExtensionManager @Inject constructor(
    timber: TimberExtension,
    mindbox: MindboxExtension,
) {

    val extensions = listOf(timber, mindbox)

}
