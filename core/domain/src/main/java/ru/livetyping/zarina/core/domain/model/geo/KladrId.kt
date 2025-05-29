package ru.livetyping.zarina.core.domain.model.geo

// TODO: [Top] Rename to FiasId
// Marked as stable on config/compose/stability_config.txt
/**
 * According to "КЛАДР РФ"
 */
@JvmInline
public value class KladrId(public val value: String) {
    public companion object {
        public val MOSCOW: KladrId
            get() = KladrId("0c5b2444-70a0-4932-980c-b4dc0d3f02b5")

        public val SAINT_PETERSBURG: KladrId
            get() = KladrId("c2deb16a-0330-4f05-821f-1d09c93331e6")
    }
}
