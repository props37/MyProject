package ru.livetyping.zarina.core.feedback.impl

import ru.livetyping.zarina.core.feedback.Feedback
import ru.livetyping.zarina.core.feedback.FeedbackEvent
import ru.uxfeedback.pub.sdk.UxFeedback

public class FeedbackImpl : Feedback {
    override fun show(event: FeedbackEvent) {
        UxFeedback.sdk?.startCampaign(event.eventName)
    }
}
