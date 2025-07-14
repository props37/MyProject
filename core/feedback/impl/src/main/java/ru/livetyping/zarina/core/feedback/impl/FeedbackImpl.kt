package ru.livetyping.zarina.core.feedback.impl

import ru.livetyping.zarina.core.feedback.Feedback
import ru.uxfeedback.pub.sdk.UxFeedback

public class FeedbackImpl : Feedback {
    override fun show(event: String) {
        UxFeedback.sdk?.startCampaign(event)
    }
}
