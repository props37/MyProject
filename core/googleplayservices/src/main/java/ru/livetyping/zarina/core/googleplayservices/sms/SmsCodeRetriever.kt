package ru.livetyping.zarina.core.googleplayservices.sms

import androidx.activity.result.ActivityResultRegistry

public interface SmsCodeRetriever {
    public fun start(sender: String, codeRegexPattern: String)

    public fun stop()

    public fun addListener(listener: Listener)

    public fun removeListener(listener: Listener)

    public fun setActivityResultRegistry(registry: ActivityResultRegistry)

    public fun unsetActivityResultRegistry(registry: ActivityResultRegistry)

    public fun release()

    public fun interface Listener {
        public fun onCodeReceived(code: String)
    }
}
