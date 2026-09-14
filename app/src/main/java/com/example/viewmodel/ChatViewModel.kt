package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.api.Candidate
import com.example.api.Content
import com.example.api.GenerateContentRequest
import com.example.api.GenerationConfig
import com.example.api.Part
import com.example.api.RetrofitClient
import com.example.api.ThinkingConfig
import retrofit2.HttpException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val isError: Boolean = false
)

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _severityCounts = MutableStateFlow(mapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0))
    val severityCounts: StateFlow<Map<Int, Int>> = _severityCounts.asStateFlow()

    private val conversationHistory = mutableListOf<Content>()

    private val systemInstruction = """
        # DIRECTIVE
        You are a Clinical Pharmacovigilance and Regulatory Informatics System. Your function is to process user-inputted adverse clinical events, cross-reference them against MedDRA taxonomy, and output exact severity grading criteria.

        # DATA DEPENDENCIES
        - Active Ontology: MedDRA (current active release) & NCI CTCAE v6.0.
        - Primary Source Data: https://dctd.cancer.gov/research/ctep-trials/for-sites/adverse-events/ctcae-v6.pdf
        - Update Polling Target: https://dctd.cancer.gov/research/ctep-trials/for-sites/adverse-events

        # EXECUTION PROTOCOL
        1. Acknowledge the search term.
        2. Verify the current operational version of CTCAE by cross-referencing the Update Polling Target. If a version post-dating v6.0 is published, utilize the updated framework and append a [VERSION UPDATE DETECTED] flag to the output header.
        3. Map the clinical query to its exact MedDRA Preferred Term (PT) and System Organ Class (SOC).
        4. Output the Grades 1-5 diagnostic/clinical criteria strictly as defined by the active CTCAE framework. Zero hallucination of severity thresholds is permitted.
    """.trimIndent()

    init {
        // Initial greeting
        _messages.value = listOf(
            ChatMessage(
                text = "Clinical Pharmacovigilance System initialized.\nReady to process adverse clinical events and cross-reference against MedDRA taxonomy / CTCAE v6.0 grading criteria.",
                isUser = false
            )
        )
    }

    fun reportAdverseEvent(term: String, grade: Int) {
        _severityCounts.update { current ->
            val newCounts = current.toMutableMap()
            newCounts[grade] = (newCounts[grade] ?: 0) + 1
            newCounts
        }
        val gradeDesc = when (grade) {
            1 -> "Mild"
            2 -> "Moderate"
            3 -> "Severe"
            4 -> "Life-threatening"
            5 -> "Death"
            else -> "Unknown"
        }
        sendMessage("Reported AE: $term (Grade $grade: $gradeDesc)\n\nPlease provide full CTCAE grading criteria and regulatory SAE assessment for this event.")
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            _messages.update { 
                it + ChatMessage(text, isUser = true) + ChatMessage("Error: Missing Gemini API Key. Please configure it in the Secrets Panel.", isUser = false, isError = true)
            }
            return
        }

        _messages.update { it + ChatMessage(text, isUser = true) }
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // Add user message to history
                conversationHistory.add(Content(role = "user", parts = listOf(Part(text = text))))
                
                val request = GenerateContentRequest(
                    contents = conversationHistory.toList(),
                    systemInstruction = Content(parts = listOf(Part(text = systemInstruction))),
                    generationConfig = GenerationConfig(
                        thinkingConfig = ThinkingConfig(thinkingLevel = "HIGH")
                    )
                )

                var response: com.example.api.GenerateContentResponse? = null
                
                try {
                    response = RetrofitClient.service.generateContent(apiKey, request)
                } catch (e: HttpException) {
                    if (e.code() == 429) {
                        // Fallback to gemini-3.5-flash on rate limit
                        val fallbackRequest = GenerateContentRequest(
                            contents = conversationHistory.toList(),
                            systemInstruction = Content(parts = listOf(Part(text = systemInstruction))),
                            generationConfig = null // Flash might not support high thinking
                        )
                        response = RetrofitClient.service.generateContentFlash(apiKey, fallbackRequest)
                    } else if (e.code() == 403 || e.code() == 404) {
                        // Fallback to gemini-1.5-pro if 3.1 is restricted or doesn't exist
                        val fallbackRequest = GenerateContentRequest(
                            contents = conversationHistory.toList(),
                            systemInstruction = Content(parts = listOf(Part(text = systemInstruction))),
                            generationConfig = null 
                        )
                        response = RetrofitClient.service.generateContent15Pro(apiKey, fallbackRequest)
                    } else {
                        throw e
                    }
                }
                
                if (response?.error != null) {
                     _messages.update { it + ChatMessage("API Error: ${response.error.message}", isUser = false, isError = true) }
                     conversationHistory.removeLast() // Rollback history
                } else if (response != null) {
                    val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    if (responseText != null) {
                        _messages.update { it + ChatMessage(responseText, isUser = false) }
                        conversationHistory.add(Content(role = "model", parts = listOf(Part(text = responseText))))
                    } else {
                        _messages.update { it + ChatMessage("Received empty response from the system.", isUser = false, isError = true) }
                        conversationHistory.removeLast()
                    }
                }
            } catch (e: Exception) {
                val errorMessage = if (e is HttpException && e.code() == 429) {
                    "Network Error: Rate limit exceeded (HTTP 429). Please wait a moment and try again."
                } else if (e is HttpException && e.code() == 403) {
                    "Network Error: Access Forbidden (HTTP 403). Please verify your Gemini API key in the Secrets Panel."
                } else {
                    "Network Error: ${e.localizedMessage}"
                }
                _messages.update { it + ChatMessage(errorMessage, isUser = false, isError = true) }
                conversationHistory.removeLast()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
