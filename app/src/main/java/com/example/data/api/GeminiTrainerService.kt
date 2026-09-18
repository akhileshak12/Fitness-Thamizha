package com.example.data.api

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    @Json(name = "contents") val contents: List<GeminiContent>,
    @Json(name = "systemInstruction") val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    @Json(name = "role") val role: String = "user",
    @Json(name = "parts") val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    @Json(name = "text") val text: String
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    @Json(name = "candidates") val candidates: List<GeminiCandidate>? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    @Json(name = "content") val content: GeminiContent? = null
)

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

class GeminiTrainerService {

    private val api: GeminiApi

    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(GeminiApi::class.java)
    }

    suspend fun askCyberTrainer(
        userPrompt: String,
        userWeightKg: Float,
        targetWeightKg: Float,
        dailyCalories: Int,
        consumedCalories: Int
    ): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "your_gemini_api_key_here") {
            // Graceful fallback with expert bulking advice if API key is not yet set in Secrets panel
            return getOfflineBulkingAdvice(userPrompt, userWeightKg, targetWeightKg)
        }

        val systemPrompt = """
            You are 'TITAN', an elite, futuristic cyber gym trainer and bulking hypertrophy expert.
            The user is currently bulking to gain muscle and weight.
            User Profile:
            - Current Weight: $userWeightKg kg
            - Target Weight: $targetWeightKg kg
            - Daily Bulking Target: $dailyCalories kcal
            - Calories Consumed Today: $consumedCalories kcal

            Style & Rules:
            1. Keep responses clear, motivating, high-energy, and scientifically accurate for muscle hypertrophy.
            2. Prioritize caloric surplus, progressive overload (6-12 rep range), adequate protein (2.0-2.2g/kg), and deep sleep for growth hormone recovery.
            3. Use a futuristic cyber aesthetic tone with cyber/HUD metaphors occasionally.
            4. Keep answers concise and directly actionable.
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(text = userPrompt))
                )
            ),
            systemInstruction = GeminiContent(
                role = "system",
                parts = listOf(GeminiPart(text = systemPrompt))
            )
        )

        return try {
            val response = api.generateContent(apiKey, request)
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            text ?: "TITAN PROTOCOL: Caloric surplus online. Ensure +350-500 kcal daily and progressive tension."
        } catch (e: Exception) {
            getOfflineBulkingAdvice(userPrompt, userWeightKg, targetWeightKg)
        }
    }

    private fun getOfflineBulkingAdvice(prompt: String, weight: Float, target: Float): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("shake") || lower.contains("liquid") ->
                "⚡ TITAN MASS DIRECTIVE: Liquid calories bypass satiety signals. Blend 500ml whole milk, 2 scoops whey protein, 80g rolled oats, 2 tbsp peanut butter, and 1 banana for an instant 950 kcal anabolic surge."
            lower.contains("plateau") || lower.contains("gain weight") || lower.contains("stuck") ->
                "⚡ HYPERTROPHY OVERLOAD: If weight is stagnant at ${weight}kg, your metabolic rate has adapted. Increase daily carbohydrate intake by 50g (+200 kcal) immediately and track weekly weigh-in trends."
            lower.contains("rest") || lower.contains("recovery") || lower.contains("sleep") ->
                "⚡ ANABOLIC SLEEP PROTOCOL: Muscle tissue expansion occurs during Stage 3/4 non-REM sleep when somatotropin (HGH) peaks. Maintain your caloric surplus even on rest days—muscles repair with yesterday's fuel!"
            lower.contains("chest") || lower.contains("bench") ->
                "⚡ CHEST HYPERTROPHY: Prioritize Incline Dumbbell Press (30° angle) with a 3-second eccentric stretch. Progressive overload in the 6-10 rep range triggers maximal sternal & clavicular fiber recruitment."
            else ->
                "⚡ TITAN DIRECTIVE: To advance from ${weight}kg to your target of ${target}kg, consistency is paramount. Target 2.2g protein per kg, eat in a 380-600 kcal surplus daily, and log every workout set with progressive volume."
        }
    }
}
