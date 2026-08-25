package com.example.ai

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

@Serializable
data class BackendAskRequest(val query: String, val userId: String? = null)

@Serializable
data class BackendAskResponse(val response: String)

class BackendAiService(private val client: HttpClient) {
    suspend fun askAi(request: BackendAskRequest): BackendAskResponse {
        return client.post("https://api.sattva.spirit/api/v1/ai/ask") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
