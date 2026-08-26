package com.example.features.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.core.ui.components.PlatformBackHandler
import com.example.core.ui.components.SattvaBottomNav
import com.example.core.ui.components.SattvaTopBar
import com.example.features.animal.AnimalDetailScreen
import com.example.features.animal.AnimalDiscoveryScreen
import com.example.features.auth.AuthScreen
import com.example.features.auth.SattvaAuthLoadingScreen
import com.example.features.donation.DonationScreen
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

@Composable
fun MainScreen(
    viewModel: SattvaViewModel = remember { SattvaViewModel() },
    onGoogleSignIn: (() -> Unit)? = null
) {
    var showSplash by remember { mutableStateOf(false) }

    val currentTab by viewModel.currentTab.collectAsState()
    val onboardingCompleted by viewModel.onboardingCompleted.collectAsState()
    val isAuthChecking by viewModel.isAuthChecking.collectAsState()
    val authUser by viewModel.authUser.collectAsState()
    val selectedPujaId by viewModel.selectedPujaId.collectAsState()
    val selectedGaushalaId by viewModel.selectedGaushalaId.collectAsState()
    val selectedAnimalId by viewModel.selectedAnimalId.collectAsState()
    val showAnimalDiscovery by viewModel.showAnimalDiscovery.collectAsState()
    val donationTarget by viewModel.donationTarget.collectAsState()

    val allPujas by viewModel.allPujas.collectAsState()
    val allGaushalas by viewModel.allGaushalas.collectAsState()
    val allAnimals by viewModel.allAnimals.collectAsState()
    val welfareUpdates by viewModel.welfareUpdates.collectAsState()
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
    val isCatalogLoading by viewModel.isCatalogLoading.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    if (showSplash) {
        SplashScreen(onSplashFinished = { showSplash = false })
    } else if (isAuthChecking) {
        SattvaAuthLoadingScreen()
    } else if (authUser == null) {
        AuthScreen(
            onSignInWithEmail = { email, pass, cb ->
                viewModel.signInWithEmail(email, pass, cb)
            },
            onSignUpWithEmail = { email, pass, name, cb ->
                viewModel.signUpWithEmail(email, pass, name, cb)
            },
            onGoogleSignIn = onGoogleSignIn
        )
    } else if (!onboardingCompleted) {
        OnboardingScreen(onFinish = { viewModel.completeOnboarding() })
    } else {
        val selectedPuja = allPujas.find { it.id == selectedPujaId }
        val selectedGaushala = allGaushalas.find { it.id == selectedGaushalaId }
        val selectedAnimal = allAnimals.find { it.id == selectedAnimalId }

        when {
            donationTarget != null -> {
                PlatformBackHandler { viewModel.closeDonation() }
                DonationScreen(
                    targetName = donationTarget!!.targetName,
                    targetType = donationTarget!!.targetType,
                    onBack = { viewModel.closeDonation() },
                    onSubmit = { amount, category ->
                        viewModel.submitDonation(amount, category)
                    }
                )
            }

            selectedAnimal != null -> {
                PlatformBackHandler { viewModel.closeAnimalDetail() }
                AnimalDetailScreen(
                    animalId = selectedAnimal.id,
                    animal = selectedAnimal,
                    onBack = { viewModel.closeAnimalDetail() },
                    onToggleFavorite = { viewModel.toggleAnimalFavorite(selectedAnimal.id, !selectedAnimal.isFavorite) },
                    onAdoptClick = {
                        viewModel.openDonation(
                            targetId = selectedAnimal.id,
                            targetName = selectedAnimal.name,
                            targetType = "ANIMAL",
                            isAdoption = true,
                            amount = 2000,
                            category = "Monthly Adoption"
                        )
                    },
                    onDonateClick = { amount ->
                        viewModel.openDonation(
                            targetId = selectedAnimal.id,
                            targetName = selectedAnimal.name,
                            targetType = "ANIMAL",
                            isAdoption = false,
                            amount = amount,
                            category = "Fodder & Healthcare"
                        )
                    }
                )
            }

            selectedPuja != null -> {
                PlatformBackHandler { viewModel.closePujaDetail() }
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
                PlatformBackHandler { viewModel.closeGaushalaDetail() }
                GaushalaDetailScreen(
                    gaushalaId = selectedGaushala.id,
                    gaushala = selectedGaushala,
                    animals = allAnimals.filter { it.gaushalaId == selectedGaushala.id }.ifEmpty { allAnimals.take(3) },
                    welfareUpdates = welfareUpdates,
                    onBack = { viewModel.closeGaushalaDetail() },
                    onAnimalClick = { animalId -> viewModel.openAnimalDetail(animalId) },
                    onSupportClick = {
                        viewModel.openDonation(
                            targetId = selectedGaushala.id,
                            targetName = selectedGaushala.name,
                            targetType = "GAUSHALA",
                            isAdoption = false,
                            amount = 500,
                            category = "General Care"
                        )
                    },
                    onAnimalSupportClick = { animal -> viewModel.openAnimalDetail(animal.id) },
                    onViewAllAnimals = { viewModel.openAnimalDiscovery() }
                )
            }

            showAnimalDiscovery -> {
                PlatformBackHandler { viewModel.closeAnimalDiscovery() }
                AnimalDiscoveryScreen(
                    animals = allAnimals,
                    isLoading = isCatalogLoading,
                    onBack = { viewModel.closeAnimalDiscovery() },
                    onAnimalClick = { animalId -> viewModel.openAnimalDetail(animalId) },
                    onSupportClick = { animal ->
                        viewModel.openDonation(
                            targetId = animal.id,
                            targetName = animal.name,
                            targetType = "ANIMAL",
                            isAdoption = false,
                            amount = 500,
                            category = "Fodder & Healthcare"
                        )
                    }
                )
            }

            else -> {
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
                            onTabSelected = { tab ->
                                viewModel.closeAnimalDiscovery()
                                viewModel.selectTab(tab)
                            }
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
                                        contributions = sevaContributions,
                                        isLoading = isCatalogLoading,
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
                                        isLoading = isCatalogLoading,
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
                                        isLoading = isCatalogLoading,
                                        animals = allAnimals,
                                        welfareUpdates = welfareUpdates,
                                        onGaushalaClick = { gaushalaId -> viewModel.openGaushalaDetail(gaushalaId) },
                                        onSupportGaushala = { gaushala ->
                                            viewModel.openDonation(
                                                targetId = gaushala.id,
                                                targetName = gaushala.name,
                                                targetType = "GAUSHALA",
                                                isAdoption = false,
                                                amount = 500,
                                                category = "General Care"
                                            )
                                        },
                                        onAnimalClick = { animalId -> viewModel.openAnimalDetail(animalId) },
                                        onSupportAnimal = { animal -> viewModel.openAnimalDetail(animal.id) },
                                        onExploreAnimalsClick = { viewModel.openAnimalDiscovery() }
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
                                        onGoogleSignIn = onGoogleSignIn,
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
