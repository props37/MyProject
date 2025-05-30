package ru.livetyping.zarina.core.domain.model.geo

// Marked as stable on config/compose/stability_config.txt
/**
 * According to "ФИАС"
 */
@JvmInline
public value class FiasId(public val value: String) {
    public companion object {
        public val MOSCOW: FiasId
            get() = FiasId("0c5b2444-70a0-4932-980c-b4dc0d3f02b5")

        public val SAINT_PETERSBURG: FiasId
            get() = FiasId("c2deb16a-0330-4f05-821f-1d09c93331e6")
    }
}
