package com.example.ai

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiContent(
    val role: String = "user",
    val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiSystemInstruction(
    val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    val temperature: Float? = 0.7f,
    val maxOutputTokens: Int? = 1000
)

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiSystemInstruction? = null,
    val generationConfig: GeminiGenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    val content: GeminiContent? = null,
    val finishReason: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<GeminiCandidate>? = null
)

interface GeminiApi {
    @POST("v1beta/models/gemini-2.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

class GeminiService {

    private val api: GeminiApi

    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(GeminiApi::class.java)
    }

    suspend fun getVedicSpiritualGuidance(
        userMessage: String,
        history: List<Pair<String, Boolean>> = emptyList(),
        userGotra: String = "Kashyapa",
        userNakshatra: String = "Rohini"
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        val systemPrompt = """
            You are "Rishi AI", the revered Vedic spiritual guide of Sattva — an authentic spiritual, cultural, and cow seva platform.
            You possess deep knowledge of Sanatana Dharma, the Vedas, Upanishads, Bhagavad Gita, Puranas, Panchang astrological timings, temple rituals, and Go-Seva (sacred cow protection).
            
            User's Spiritual Profile:
            - Gotra: $userGotra
            - Nakshatra: $userNakshatra
            
            Guidelines:
            1. Respond with warmth, reverence ("Namaste", "Om Shanti", "May Mahadev/Bhagwan bless you"), and concise wisdom.
            2. Explain the spiritual significance of rituals, shlokas, puja offerings, and virtues of Go-Seva.
            3. If user asks for a Sankalpa mantra or blessing, compose an authentic personalized Sanskrit/English Sankalpa.
            4. Keep responses structured, easy to read, with bold highlights and peaceful tone.
        """.trimIndent()

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Provide intelligent local Vedic answers
            return@withContext getLocalVedicResponse(userMessage, userGotra, userNakshatra)
        }

        try {
            val contents = mutableListOf<GeminiContent>()
            for ((text, isUser) in history.takeLast(6)) {
                contents.add(
                    GeminiContent(
                        role = if (isUser) "user" else "model",
                        parts = listOf(GeminiPart(text = text))
                    )
                )
            }
            contents.add(
                GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(text = userMessage))
                )
            )

            val request = GeminiRequest(
                contents = contents,
                systemInstruction = GeminiSystemInstruction(
                    parts = listOf(GeminiPart(text = systemPrompt))
                ),
                generationConfig = GeminiGenerationConfig(temperature = 0.6f, maxOutputTokens = 800)
            )

            val response = api.generateContent(apiKey, request)
            val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            reply ?: getLocalVedicResponse(userMessage, userGotra, userNakshatra)
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
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = "Compose a sacred, personalized Vedic Sankalpa statement for $devoteeName of $gotra Gotra, $nakshatra Nakshatra, performing $pujaTitle for the wish/intent: '$intent'. Include Sanskrit invocations (e.g., 'मम कायिक वाचिक मानसिक...', 'ॐ तत्सत्') and English translation with blessings."
                val response = api.generateContent(
                    apiKey,
                    GeminiRequest(
                        contents = listOf(
                            GeminiContent(
                                role = "user",
                                parts = listOf(GeminiPart(text = prompt))
                            )
                        ),
                        generationConfig = GeminiGenerationConfig(temperature = 0.5f, maxOutputTokens = 400)
                    )
                )
                val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!text.isNullOrBlank()) return@withContext text
            } catch (_: Exception) {}
        }

        // Local high-quality Vedic Sankalpa fallback
        """
        ॥ ॐ तत्सत् श्रीब्रह्मणो द्वितीयपरार्धे श्रीश्वेतवाराहकल्पे ॥
        
        Sankalpa for $pujaTitle:
        "अहं $devoteeName, $gotra गोत्रोत्पन्नः, $nakshatra नक्षत्रे जातः, मम समस्त कायिक-वाचिक-मानसिक पापनिवृत्तिपूर्वक सर्व-मनोरथ सिद्धयर्थे श्रीभगवतः प्रीत्यर्थं संकल्पं समर्पयामि।"
        
        Translation & Blessings:
        "I, $devoteeName, born in the sacred lineage of $gotra Gotra and under the radiant stars of $nakshatra Nakshatra, solemnly offer this Sankalpa during $pujaTitle for the fulfillment of: '$intent', seeking the eternal grace, peace, and health of Bhagwan."
        """.trimIndent()
    }

    private fun getLocalVedicResponse(userMessage: String, gotra: String, nakshatra: String): String {
        val lower = userMessage.lowercase()
        return when {
            lower.contains("sankalpa") || lower.contains("mantra") -> {
                """
                **ॐ नमः शिवाय | Sacred Sankalpa Guidance**
                
                For a devotee belonging to **$gotra Gotra** under **$nakshatra Nakshatra**, chanting the *Maha Mrityunjaya Mantra* or *Gayatri Mantra* during Brahma Muhurta (4:30 AM – 6:00 AM) cleanses karmic burdens.
                
                *Mantra:*
                `ॐ त्र्यम्बकं यजामहे सुगन्धिं पुष्टिवर्धनम् । उर्वारुकमिव बन्धनान्मृत्य pushyāmritāt ॥`
                
                May your sacred intentions bring harmony to your lineage and household.
                """.trimIndent()
            }
            lower.contains("rudrabhishek") || lower.contains("shiva") -> {
                """
                **The Sacred Glory of Maha Rudrabhishek**
                
                Rudrabhishek at Kashi Vishwanath is described in the *Shiva Purana* as the ultimate purification. When sacred Panchamrit (milk, honey, ghee, curd, and sugar) is poured upon the Jyotirlinga, all nine planetary afflictions are neutralized.
                
                *Key Inclusions in your Sattva Booking:*
                - Consecrated Bhasma and Gangajal Prasad delivered to your doorstep.
                - Purohits chanting with individual Gotra उच्चारण (pronunciation).
                """.trimIndent()
            }
            lower.contains("cow") || lower.contains("gaushala") || lower.contains("seva") -> {
                """
                **Go-Seva: The Supreme Merit in Sanatana Dharma**
                
                According to the *Padma Purana*, all 33 Koti divine energies reside in the sacred cow (Gau Mata). Supporting fodder and healthcare at sanctuaries like **Shri Krishna Gaushala (Vrindavan)** generates boundless spiritual merit (Punya) and removes obstacles in one's life.
                
                *"सर्वतीर्थमयी गावो..." — In serving cows, one visits all holy tirthas.*
                """.trimIndent()
            }
            lower.contains("panchang") || lower.contains("tithi") || lower.contains("nakshatra") -> {
                """
                **Today's Vedic Astrology Insights**
                
                - **Tithi:** Shukla Paksha Dashami
                - **Nakshatra:** Rohini (Ruled by Lord Brahma & Moon, symbolizing growth and auspicious beginnings)
                - **Auspicious Muhurta:** Abhijit Muhurta (11:58 AM - 12:48 PM)
                - **Rahu Kaal:** 4:30 PM - 6:00 PM (Avoid starting new worldly transactions)
                
                May the stars align beneficially for your family and spiritual pursuits.
                """.trimIndent()
            }
            else -> {
                """
                **Namaste, Devotee.**
                
                In the Bhagavad Gita (9.26), Lord Krishna states:
                *"पत्रं पुष्पं फलं तोयं यो मे भक्त्या प्रयच्छति..."*
                (Whoever offers Me with devotion a leaf, a flower, a fruit, or even water, I accept it with love).
                
                Whether you are offering a sacred **Sankalpa** for a temple Puja or sponsoring **Go-Seva** for rescued calves, your pure devotion is the highest offering.
                
                How may I guide your spiritual path today? Feel free to ask about rituals, mantra pronunciation, temple histories, or Vedic calendar timings.
                """.trimIndent()
            }
        }
    }
}
