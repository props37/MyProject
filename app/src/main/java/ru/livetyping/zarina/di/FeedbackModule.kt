package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.feedback.Feedback
import ru.livetyping.zarina.core.feedback.impl.FeedbackImpl

@Module
@InstallIn(SingletonComponent::class)
class FeedbackModule {
    private val feedback by lazy { FeedbackImpl() }

    @Provides
    fun provideFeedback(): Feedback = feedback
}
