package ru.livetyping.zarina.data.user.impl.local.review

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

internal class InAppReviewDataHolderImpl @Inject constructor() : InAppReviewDataHolder {
    private val requests = Channel<Unit>(Channel.CONFLATED)

    override fun getInAppReviewRequestFlow(): Flow<Unit> {
        return requests.receiveAsFlow()
    }

    override fun requestInAppReview() {
        requests.trySend(Unit)
    }
}
