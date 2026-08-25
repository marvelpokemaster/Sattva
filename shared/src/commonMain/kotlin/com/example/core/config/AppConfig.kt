package com.example.core.config

object AppConfig {
    /**
     * Base URL for the backend API service (Cloudflare Worker).
     * Points to the production Cloudflare Worker deployment.
     */
    var backendBaseUrl: String = "https://utsavam-backend.utsavam-api.workers.dev"
    
    val aiAskEndpoint: String
        get() = "$backendBaseUrl/api/v1/ai/ask"
}
