package com.example.features.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.ui.theme.DeepMoss
import com.example.core.ui.theme.MutedGold
import com.example.core.ui.theme.RitualClay
import com.example.core.ui.theme.SerifFontFamily
import com.example.core.ui.theme.SurfaceIvory
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageUrl: String
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = listOf(
        OnboardingPage(
            title = "Connect with the Divine",
            description = "Experience authentic pujas and discover ancient temples from the comfort of your home.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDiu4rHiOOhOm08vo4xawZdjGfKr6Vt1x_hY9JCLpAp2gdlmMc6aPL3bwGj1dF7dhcRvgaWr-6my9gMgyrlOLUETVTnNyA3QiALhZMSF9yR9VyxWkKyFbi5qMhY57XNXqiumkcawWXyThS92nL-n6vmdBD04Uus1tj70fmvb-SzFoulBCsuizfy63V-k4v_ComoSTcw2L04vwm6VNcBGtrNR8XLMI2q11i8eD-cfrnzxjQDkEtnF7o"
        ),
        OnboardingPage(
            title = "Serve the Sacred",
            description = "Participate in Seva by supporting Gaushalas and animal welfare initiatives across the country.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCMbEOZmuGd0tcZGSy9vWF9pm-cEsK0SM4zjKIw1lUJiKomwvoNCiJ-KQtm5VZ1gILmX0ddC1j9i7DQjHbgbt-SL6bqvtLsbYV_wWzwDJPYr0ORAKUf1ONs9tOqpKn5Wccgza9WETnAP2CIHq3SF8qcpjxkl0iGdwfSxuvMZmAqpPWZQY-_Bj8jpeFItAIOAyI89P6o8qJ5MG5QFK02JCJFf66VWMSM6yGjlGqtvM9H4JeNfTESKds"
        ),
        OnboardingPage(
            title = "Celebrate Culture",
            description = "Immerse yourself in stories, rituals, and the rich heritage of spiritual traditions.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBaCGZWMaxQqXV0H27zuozMe8nCQQdYKq-0M2HpfI02xD6h3r6aSaaPZ6FxbluBHqfpVBl074991SsrHNSIazVb4WCo2jusXBqwhcV-08tJaZYpTDbs_ABejUF3oRJsUWGzaCZrzb2QKQ0g5340JXAVMWXhus9o1PpCxt15n5MzKbBqeOUfxVC1eLUI-yrXkIMwmojA5MzLNN7gI3pe2mE2yECduzpyviAnqyxxU9DKUXuPwq3bnaU"
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceIvory)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar with Brand & Skip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sattva",
                    fontFamily = SerifFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = RitualClay
                )

                TextButton(
                    onClick = onFinish,
                    modifier = Modifier.testTag("skip_onboarding_btn")
                ) {
                    Text(
                        text = "Skip",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }

            // Pager content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { pageIdx ->
                val page = pages[pageIdx]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Image with rounded corners and subtle shadow
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                            .shadow(12.dp, RoundedCornerShape(28.dp))
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color.LightGray)
                    ) {
                        AsyncImage(
                            model = page.imageUrl,
                            contentDescription = page.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = page.title,
                        fontFamily = SerifFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        lineHeight = 36.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = page.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // Bottom Controls (Dots + Next Button)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Page Indicator Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(if (isSelected) 24.dp else 8.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) RitualClay else RitualClay.copy(alpha = 0.25f))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (pagerState.currentPage < pages.size - 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onFinish()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("onboarding_action_btn"),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RitualClay,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (pagerState.currentPage == pages.size - 1) "Get Started" else "Next",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
