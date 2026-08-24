package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiService
import com.example.data.local.AppDatabase
import com.example.data.model.AnimalResident
import com.example.data.model.ChatMessage
import com.example.data.model.DailyWisdom
import com.example.data.model.FamilyMember
import com.example.data.model.Gaushala
import com.example.data.model.PanchangInfo
import com.example.data.model.Puja
import com.example.data.model.SevaContribution
import com.example.data.model.UserProfile
import com.example.data.repository.SattvaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab {
    HOME,
    EXPLORE,
    SEVA,
    VEDIC_AI,
    PROFILE
}

class SattvaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SattvaRepository
    private val geminiService = GeminiService()

    // Navigation & Tab State
    private val _currentTab = MutableStateFlow(MainTab.HOME)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _onboardingCompleted = MutableStateFlow(false)
    val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted.asStateFlow()

    private val _selectedPujaId = MutableStateFlow<String?>(null)
    val selectedPujaId: StateFlow<String?> = _selectedPujaId.asStateFlow()

    private val _selectedGaushalaId = MutableStateFlow<String?>(null)
    val selectedGaushalaId: StateFlow<String?> = _selectedGaushalaId.asStateFlow()

    private val _selectedAnimalId = MutableStateFlow<String?>(null)
    val selectedAnimalId: StateFlow<String?> = _selectedAnimalId.asStateFlow()

    // Search query & filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedPujaCategory = MutableStateFlow("All")
    val selectedPujaCategory: StateFlow<String> = _selectedPujaCategory.asStateFlow()

    private val _gaushalaViewMode = MutableStateFlow("List") // "List" or "Map"
    val gaushalaViewMode: StateFlow<String> = _gaushalaViewMode.asStateFlow()

    // Family Members in Profile
    private val _familyMembers = MutableStateFlow<List<FamilyMember>>(emptyList())
    val familyMembers: StateFlow<List<FamilyMember>> = _familyMembers.asStateFlow()

    // AI Chat Messages
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "Namaste, Devotee! I am Rishi AI, your spiritual companion at Sattva. How may I assist your spiritual path, Vedic rituals, or Go-Seva inquiries today?",
                isUser = false
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // AI Sankalpa Generated Text
    private val _generatedSankalpa = MutableStateFlow("")
    val generatedSankalpa: StateFlow<String> = _generatedSankalpa.asStateFlow()

    private val _isGeneratingSankalpa = MutableStateFlow(false)
    val isGeneratingSankalpa: StateFlow<Boolean> = _isGeneratingSankalpa.asStateFlow()

    // Success Notification Dialog / Toast
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Repository Flows
    val allPujas: StateFlow<List<Puja>>
    val allGaushalas: StateFlow<List<Gaushala>>
    val allAnimals: StateFlow<List<AnimalResident>>
    val urgentAnimals: StateFlow<List<AnimalResident>>
    val userProfile: StateFlow<UserProfile?>
    val sevaContributions: StateFlow<List<SevaContribution>>

    val todayPanchang: PanchangInfo
    val todayWisdom: DailyWisdom

    init {
        val database = AppDatabase.getInstance(application)
        repository = SattvaRepository(database)

        allPujas = repository.allPujas.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        allGaushalas = repository.allGaushalas.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        allAnimals = repository.allAnimals.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        urgentAnimals = repository.urgentAnimals.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        userProfile = repository.userProfile.stateIn(viewModelScope, SharingStarted.Lazily, UserProfile())
        sevaContributions = repository.allContributions.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

        todayPanchang = repository.getTodayPanchang()
        todayWisdom = repository.getTodayWisdom()
        _familyMembers.value = repository.getDefaultFamilyMembers()

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun completeOnboarding() {
        _onboardingCompleted.value = true
    }

    fun selectTab(tab: MainTab) {
        _currentTab.value = tab
        // Reset sub-screen selections when switching primary tabs
        if (tab != MainTab.HOME && tab != MainTab.EXPLORE) {
            _selectedPujaId.value = null
        }
        if (tab != MainTab.SEVA) {
            _selectedGaushalaId.value = null
            _selectedAnimalId.value = null
        }
    }

    fun openPujaDetail(pujaId: String) {
        _selectedPujaId.value = pujaId
    }

    fun closePujaDetail() {
        _selectedPujaId.value = null
        _generatedSankalpa.value = ""
    }

    fun openGaushalaDetail(gaushalaId: String) {
        _selectedGaushalaId.value = gaushalaId
        _selectedAnimalId.value = null
    }

    fun closeGaushalaDetail() {
        _selectedGaushalaId.value = null
    }

    fun openAnimalDetail(animalId: String) {
        _selectedAnimalId.value = animalId
    }

    fun closeAnimalDetail() {
        _selectedAnimalId.value = null
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setPujaCategory(category: String) {
        _selectedPujaCategory.value = category
    }

    fun setGaushalaViewMode(mode: String) {
        _gaushalaViewMode.value = mode
    }

    fun togglePujaBookmark(pujaId: String, current: Boolean) {
        viewModelScope.launch {
            repository.togglePujaBookmark(pujaId, !current)
        }
    }

    fun toggleAnimalFavorite(animalId: String, current: Boolean) {
        viewModelScope.launch {
            repository.toggleAnimalFavorite(animalId, !current)
        }
    }

    fun bookPuja(pujaId: String, gotra: String, name: String, date: String) {
        viewModelScope.launch {
            repository.bookPuja(pujaId, gotra, name, date)
            _toastMessage.value = "Sankalpa successfully registered! Prasad will be dispatched."
        }
    }

    fun contributeToAnimal(animalId: String, amount: Int, animalName: String, category: String = "Fodder & Nutrition") {
        viewModelScope.launch {
            repository.contributeToAnimal(animalId, amount, animalName, category)
            _toastMessage.value = "Dhanyawad! ₹$amount contributed for $animalName's seva."
        }
    }

    fun contributeToGaushala(gaushalaId: String, amount: Int, gaushalaName: String, category: String = "General Care") {
        viewModelScope.launch {
            repository.contributeToGaushala(gaushalaId, amount, gaushalaName, category)
            _toastMessage.value = "Dhanyawad! ₹$amount contributed to $gaushalaName."
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun generateAiSankalpa(pujaTitle: String, devoteeName: String, gotra: String, nakshatra: String, intent: String) {
        viewModelScope.launch {
            _isGeneratingSankalpa.value = true
            val result = geminiService.generatePersonalizedSankalpa(pujaTitle, devoteeName, gotra, nakshatra, intent)
            _generatedSankalpa.value = result
            _isGeneratingSankalpa.value = false
        }
    }

    fun sendAiChatMessage(userText: String) {
        if (userText.isBlank()) return
        val userMsg = ChatMessage(text = userText, isUser = true)
        _chatMessages.value = _chatMessages.value + userMsg
        _isAiThinking.value = true

        viewModelScope.launch {
            val history = _chatMessages.value.map { it.text to it.isUser }
            val currentProfile = userProfile.value
            val gotra = currentProfile?.gotra ?: "Kashyapa"
            val nakshatra = currentProfile?.nakshatra ?: "Rohini"

            val responseText = geminiService.getVedicSpiritualGuidance(
                userMessage = userText,
                history = history,
                userGotra = gotra,
                userNakshatra = nakshatra
            )

            val modelMsg = ChatMessage(text = responseText, isUser = false)
            _chatMessages.value = _chatMessages.value + modelMsg
            _isAiThinking.value = false
        }
    }

    fun updateSpiritualIdentity(gotra: String, nakshatra: String, rashi: String) {
        viewModelScope.launch {
            repository.updateSpiritualIdentity(gotra, nakshatra, rashi)
            _toastMessage.value = "Spiritual Identity updated successfully."
        }
    }

    fun addFamilyMember(member: FamilyMember) {
        _familyMembers.value = _familyMembers.value + member
        _toastMessage.value = "Family member ${member.name} added for Sankalpas."
    }
}
