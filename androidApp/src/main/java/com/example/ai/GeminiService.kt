package com.example.ai

import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class GeminiService {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Assuming local emulator/backend is running on 10.0.2.2:8000 for local testing
    private val api: BackendAiService = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000/")
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(BackendAiService::class.java)

    private val systemPrompt = """
        You are Rishi, an AI assistant representing Sattva/Utsavam...
    """.trimIndent()

    suspend fun chatWithRishi(
        userMessage: String,
        history: List<Pair<String, Boolean>>,
        userGotra: String,
        userNakshatra: String
    ): String = withContext(Dispatchers.IO) {
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
    ): String = withContext(Dispatchers.IO) {
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
