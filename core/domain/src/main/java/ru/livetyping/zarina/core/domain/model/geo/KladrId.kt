package ru.livetyping.zarina.core.domain.model.geo

// Marked as stable on config/compose/stability_config.txt
/**
 * According to "КЛАДР РФ"
 */
@JvmInline
public value class KladrId(public val value: String) {
    public companion object {
        public val MOSCOW: KladrId
            get() = KladrId("7700000000000")

        public val SAINT_PETERSBURG: KladrId
            get() = KladrId("7800000000000")
    }
}
