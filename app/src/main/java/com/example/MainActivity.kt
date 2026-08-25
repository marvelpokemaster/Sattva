package com.example

import android.os.Bundle
import com.example.features.main.MainTab
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.ui.components.SattvaBottomNav
import com.example.core.ui.components.SattvaTopBar
import com.example.features.animal.AnimalDetailScreen
import com.example.features.home.ExploreScreen
import com.example.features.gaushala.GaushalaDetailScreen
import com.example.features.gaushala.GaushalaDiscoveryScreen
import com.example.features.home.HomeScreen
import com.example.features.profile.OnboardingScreen
import com.example.features.profile.ProfileScreen
import com.example.features.puja.PujaDetailScreen
import com.example.features.puja.PujaDiscoveryScreen
import com.example.features.home.SplashScreen
import com.example.features.ai.VedicAiScreen
import com.example.core.ui.theme.SattvaTheme
import com.example.features.main.SattvaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SattvaTheme {
                SattvaApp()
            }
        }
    }
}

@Composable
fun SattvaApp(
    viewModel: SattvaViewModel = viewModel()
) {
    var showSplash by remember { mutableStateOf(true) }

    val currentTab by viewModel.currentTab.collectAsState()
    val onboardingCompleted by viewModel.onboardingCompleted.collectAsState()
    val selectedPujaId by viewModel.selectedPujaId.collectAsState()
    val selectedGaushalaId by viewModel.selectedGaushalaId.collectAsState()
    val selectedAnimalId by viewModel.selectedAnimalId.collectAsState()

    val authUser by viewModel.authUser.collectAsState()
    val allPujas by viewModel.allPujas.collectAsState()
    val allGaushalas by viewModel.allGaushalas.collectAsState()
    val allAnimals by viewModel.allAnimals.collectAsState()
    val urgentAnimals by viewModel.urgentAnimals.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val sevaContributions by viewModel.sevaContributions.collectAsState()
    val familyMembers by viewModel.familyMembers.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedPujaCategory by viewModel.selectedPujaCategory.collectAsState()
    val gaushalaViewMode by viewModel.gaushalaViewMode.collectAsState()

    val chatMessages by viewModel.chatMessages.collectAsState()
    val isAiThinking by viewModel.isAiThinking.collectAsState()
    val generatedSankalpa by viewModel.generatedSankalpa.collectAsState()
    val isGeneratingSankalpa by viewModel.isGeneratingSankalpa.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    if (showSplash) {
        SplashScreen(onSplashFinished = { showSplash = false })
    } else if (!onboardingCompleted) {
        OnboardingScreen(onFinish = { viewModel.completeOnboarding() })
    } else {
        // Active Sub-screen checks (Details view)
        val selectedPuja = allPujas.find { it.id == selectedPujaId }
        val selectedGaushala = allGaushalas.find { it.id == selectedGaushalaId }
        val selectedAnimal = allAnimals.find { it.id == selectedAnimalId }

        when {
            selectedAnimal != null -> {
                BackHandler { viewModel.closeAnimalDetail() }
                AnimalDetailScreen(
                    animal = selectedAnimal,
                    onBackClick = { viewModel.closeAnimalDetail() },
                    onToggleFavorite = { viewModel.toggleAnimalFavorite(selectedAnimal.id, it) },
                    onContribute = { amount ->
                        viewModel.contributeToAnimal(selectedAnimal.id, amount, selectedAnimal.name)
                    }
                )
            }

            selectedPuja != null -> {
                BackHandler { viewModel.closePujaDetail() }
                PujaDetailScreen(
                    puja = selectedPuja,
                    userProfile = userProfile,
                    generatedSankalpa = generatedSankalpa,
                    isGeneratingSankalpa = isGeneratingSankalpa,
                    onBackClick = { viewModel.closePujaDetail() },
                    onToggleBookmark = { viewModel.togglePujaBookmark(selectedPuja.id, it) },
                    onGenerateAiSankalpa = { title, devotee, gotra, nakshatra, intent ->
                        viewModel.generateAiSankalpa(title, devotee, gotra, nakshatra, intent)
                    },
                    onConfirmBooking = { pujaId, gotra, name, date ->
                        viewModel.bookPuja(pujaId, gotra, name, date)
                    }
                )
            }

            selectedGaushala != null -> {
                BackHandler { viewModel.closeGaushalaDetail() }
                GaushalaDetailScreen(
                    gaushala = selectedGaushala,
                    residents = allAnimals.filter { it.gaushalaId == selectedGaushala.id }.ifEmpty { allAnimals.take(3) },
                    onBackClick = { viewModel.closeGaushalaDetail() },
                    onAnimalClick = { animalId -> viewModel.openAnimalDetail(animalId) },
                    onContribute = { amount, category ->
                        viewModel.contributeToGaushala(selectedGaushala.id, amount, selectedGaushala.name, category)
                    }
                )
            }

            else -> {
                // Main Application Scaffold with TopBar and Floating BottomNav
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        SattvaTopBar(
                            avatarUrl = userProfile?.avatarUrl ?: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
                            onAvatarClick = { viewModel.selectTab(MainTab.PROFILE) },
                            onAiClick = { viewModel.selectTab(MainTab.VEDIC_AI) },
                            onNotificationsClick = { viewModel.selectTab(MainTab.PROFILE) }
                        )
                    },
                    bottomBar = {
                        SattvaBottomNav(
                            selectedTab = currentTab,
                            onTabSelected = { tab -> viewModel.selectTab(tab) }
                        )
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Crossfade(
                            targetState = currentTab,
                            label = "tab_crossfade"
                        ) { tab ->
                            when (tab) {
                                MainTab.HOME -> {
                                    val featuredPuja = allPujas.find { it.isFeatured } ?: allPujas.firstOrNull()
                                    HomeScreen(
                                        userProfile = userProfile,
                                        featuredPuja = featuredPuja,
                                        animalsNeedingSeva = if (urgentAnimals.isNotEmpty()) urgentAnimals else allAnimals,
                                        todayPanchang = viewModel.todayPanchang,
                                        todayWisdom = viewModel.todayWisdom,
                                        onPujaClick = { pujaId -> viewModel.openPujaDetail(pujaId) },
                                        onAnimalClick = { animalId -> viewModel.openAnimalDetail(animalId) },
                                        onViewAllPujas = { viewModel.selectTab(MainTab.EXPLORE) },
                                        onViewAllSeva = { viewModel.selectTab(MainTab.SEVA) },
                                        onAskAiWisdom = {
                                            viewModel.selectTab(MainTab.VEDIC_AI)
                                            viewModel.sendAiChatMessage("Please elaborate on today's Vedic wisdom: “${viewModel.todayWisdom.quote}”")
                                        },
                                        onViewProfile = { viewModel.selectTab(MainTab.PROFILE) },
                                        onSupportAnimal = { animal -> viewModel.openAnimalDetail(animal.id) }
                                    )
                                }

                                MainTab.EXPLORE -> {
                                    PujaDiscoveryScreen(
                                        pujas = allPujas,
                                        selectedCategory = selectedPujaCategory,
                                        searchQuery = searchQuery,
                                        onCategorySelected = { viewModel.setPujaCategory(it) },
                                        onSearchChanged = { viewModel.setSearchQuery(it) },
                                        onPujaClick = { pujaId -> viewModel.openPujaDetail(pujaId) }
                                    )
                                }

                                MainTab.SEVA -> {
                                    GaushalaDiscoveryScreen(
                                        gaushalas = allGaushalas,
                                        viewMode = gaushalaViewMode,
                                        onViewModeChange = { viewModel.setGaushalaViewMode(it) },
                                        onGaushalaClick = { gaushalaId -> viewModel.openGaushalaDetail(gaushalaId) },
                                        onSupportGaushala = { gaushala -> viewModel.openGaushalaDetail(gaushala.id) }
                                    )
                                }

                                MainTab.VEDIC_AI -> {
                                    VedicAiScreen(
                                        messages = chatMessages,
                                        isThinking = isAiThinking,
                                        onSendMessage = { text -> viewModel.sendAiChatMessage(text) }
                                    )
                                }

                                MainTab.PROFILE -> {
                                    ProfileScreen(
                                        userProfile = userProfile,
                                        firebaseUser = authUser,
                                        allPujas = allPujas,
                                        contributions = sevaContributions,
                                        familyMembers = familyMembers,
                                        onUpdateSpiritualIdentity = { gotra, nakshatra, rashi ->
                                            viewModel.updateSpiritualIdentity(gotra, nakshatra, rashi)
                                        },
                                        onAddFamilyMember = { member ->
                                            viewModel.addFamilyMember(member)
                                        },
                                        onSignInWithEmail = { email, pass, cb ->
                                            viewModel.signInWithEmail(email, pass, cb)
                                        },
                                        onSignUpWithEmail = { email, pass, name, cb ->
                                            viewModel.signUpWithEmail(email, pass, name, cb)
                                        },
                                        onSignInAsDevotee = { name, cb ->
                                            viewModel.signInAsDevotee(name, cb)
                                        },
                                        onSignOut = {
                                            viewModel.signOut()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
