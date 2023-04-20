package ru.zarina.zarina.data.product.remote.api

import io.ktor.client.HttpClient
import javax.inject.Inject

class KtorZarinaProductApi @Inject constructor(
    private val client: HttpClient,
) : IZarinaProductApi
