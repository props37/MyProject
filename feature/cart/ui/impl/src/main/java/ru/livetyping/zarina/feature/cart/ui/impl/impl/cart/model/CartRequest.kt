package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model

import ru.livetyping.zarina.core.coroutinesutil.FlowRequest

internal enum class CartRequest : FlowRequest { LOADING, REFRESHING, PULL_REFRESHING }
