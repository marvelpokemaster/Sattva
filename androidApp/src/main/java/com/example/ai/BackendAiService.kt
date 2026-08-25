package com.example.ai

import retrofit2.http.Body
import retrofit2.http.POST

data class BackendAskRequest(val query: String, val userId: String? = null)
data class BackendAskResponse(val response: String)

interface BackendAiService {
    @POST("api/v1/ai/ask")
    suspend fun askAi(@Body request: BackendAskRequest): BackendAskResponse
}
