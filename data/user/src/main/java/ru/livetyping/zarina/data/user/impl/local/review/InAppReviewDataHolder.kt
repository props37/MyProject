package ru.livetyping.zarina.data.user.impl.local.review

import kotlinx.coroutines.flow.Flow

internal interface InAppReviewDataHolder {
    fun getInAppReviewRequestFlow(): Flow<Unit>

    fun requestInAppReview()
}
