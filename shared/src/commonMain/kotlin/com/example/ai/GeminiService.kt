package com.example.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class GeminiService {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val api = BackendAiService(httpClient)

    private val systemPrompt = """
        You are Rishi, an AI assistant representing Sattva/Utsavam...
    """.trimIndent()

    suspend fun chatWithRishi(
        userMessage: String,
        history: List<Pair<String, Boolean>>,
        userGotra: String,
        userNakshatra: String
    ): String = withContext(Dispatchers.Default) {
        try {
            val queryContext = "User Gotra: $userGotra, User Nakshatra: $userNakshatra. History: $history. Question: $userMessage"
            val response = api.askAi(BackendAskRequest(query=queryContext))
            response.response
        } catch (e: Exception) {
            getLocalVedicResponse(userMessage, userGotra, userNakshatra)
        }
    }

    suspend fun generatePersonalizedSankalpa(
        pujaTitle: String,
        devoteeName: String,
        gotra: String,
        nakshatra: String,
        intent: String
    ): String = withContext(Dispatchers.Default) {
        try {
            val prompt = "Compose a sacred, personalized Vedic Sankalpa statement for $devoteeName of $gotra Gotra, $nakshatra Nakshatra, performing $pujaTitle for the wish/intent: '$intent'. Include Sanskrit invocations (e.g., 'मम कायिक वाचिक मानसिक...', 'ॐ तत्सत्') and English translation with blessings."
            val response = api.askAi(BackendAskRequest(query=prompt))
            response.response
        } catch (e: Exception) {
            // Local high-quality Vedic Sankalpa fallback
            """
            ॥ ॐ तत्सत् श्रीब्रह्मणो द्वितीयपरार्धे श्रीश्वेतवाराहकल्पे ॥
            
            Sankalpa for $pujaTitle:
            "अहं $devoteeName, $gotra गोत्रोत्पन्नः, $nakshatra नक्षत्रे जातः, मम समस्त कायिक-वाचिक-मानसिक पापनिवृत्तिपूर्वक सर्व-मनोरथ सिद्धयर्थे श्रीभगवतः प्रीत्यर्थं संकल्पं समर्पयामि।"
            
            Translation & Blessings:
            "I, $devoteeName, born in the sacred lineage of $gotra Gotra and under the radiant stars of $nakshatra Nakshatra, solemnly offer this Sankalpa during $pujaTitle for the fulfillment of: '$intent', seeking the eternal grace, peace, and health of Bhagwan."
            """.trimIndent()
        }
    }

    private fun getLocalVedicResponse(userMessage: String, gotra: String, nakshatra: String): String {
        return "Namaste. The stars (Nakshatra: $nakshatra, Gotra: $gotra) guide your path. Please ensure your connection is active for personalized wisdom."
    }
}
