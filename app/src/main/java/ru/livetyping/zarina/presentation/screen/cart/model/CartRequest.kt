package ru.livetyping.zarina.presentation.screen.cart.model

import ru.livetyping.zarina.util.library.coroutines.FlowRequester

enum class CartRequest : FlowRequester.Request { LOADING, REFRESHING }
