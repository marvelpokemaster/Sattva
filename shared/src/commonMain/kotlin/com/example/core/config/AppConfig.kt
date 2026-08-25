package com.example.core.config

object AppConfig {
    /**
     * Base URL for the backend API service (FastAPI).
     * Defaults to the production gateway or local development host.
     */
    var backendBaseUrl: String = "https://api.sattva.spirit"
    
    val aiAskEndpoint: String
        get() = "$backendBaseUrl/api/v1/ai/ask"
}
