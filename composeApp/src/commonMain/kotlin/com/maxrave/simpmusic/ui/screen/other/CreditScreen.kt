package com.maxrave.simpmusic.ui.screen.other

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.maxrave.simpmusic.expect.openUrl
import com.maxrave.simpmusic.ui.theme.typo
import com.maxrave.simpmusic.utils.VersionManager
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import simpmusic.composeapp.generated.resources.*

/**
 * ============================================================================
 * Project: Naeem Music (Custom Fork of SimpMusic)
 * Architect & Developer: DrakoXNaeem (Naeem Ansari)
 * File: CreditScreen.kt
 * Description: Production-ready fully styled credit & about screen featuring 
 * professional gradients, interactive social cards, secure external links, 
 * and custom Material 3 UI design components.
 * ============================================================================
 */

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalHazeMaterialsApi::class,
)
@Composable
fun CreditScreen(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    val hazeState = rememberHazeState()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F051D),
                        Color(0xFF1A0B2E),
                        Color(0xFF05020A)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Top spacing to account for TopAppBar overlay
            Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding() + 90.dp))

            // App Icon Container with Glow/Border Styling
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF00E5FF), Color(0xFFBD00FF), Color(0xFFFF007A))
                        )
                    )
                    .padding(3.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.app_icon),
                    contentDescription = "Naeem Music App Icon",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Professional App Title Branding
            Text(
                text = "Naeem Music",
                style = typo().titleLarge,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // App Version Badge
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF2A1B4E),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = stringResource(
                        Res.string.version_format,
                        VersionManager.getVersionName(),
                    ),
                    style = typo().bodySmall,
                    fontSize = 13.sp,
                    color = Color(0xFF00E5FF),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Developer Branding: DrakoXNaeem Style
            Text(
                text = "Designed & Developed by DrakoXNaeem",
                style = typo().bodyMedium,
                fontWeight = FontWeight.Medium,
                textDecoration = TextDecoration.Underline,
                color = Color(0xFFFF79C6),
                modifier = Modifier
                    .clickable {
                        openUrl("https://magma-portfolio-sigma.vercel.app")
                    }
                    .padding(8.dp),
            )

            Spacer(modifier = Modifier.height(20.dp))

            // App Description Section Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF160D2D).copy(alpha = 0.8f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "System Architecture & Overview",
                        style = typo().titleMedium,
                        color = Color(0xFF00E5FF),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Naeem Music is a high-performance, cross-platform audio streaming solution powered by advanced modern technologies. Built with security, speed, and precision automation in mind.",
                        style = typo().bodyMedium,
                        color = Color(0xFFD1C4E9),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section Header: Community & Social Links
            Text(
                text = "MEDIA & SOCIALS",
                style = typo().labelLarge,
                color = Color(0xFF9575CD),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 8.dp),
                textAlign = TextAlign.Start
            )

            // Links and Buttons Section
            CompositionLocalProvider(
                LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
            ) {
                // Official Website Button
                CreditMenuButton(
                    title = "Official Portfolio Website",
                    subtitle = "magma-portfolio-sigma.vercel.app",
                    iconSymbol = "🌐",
                    onClick = { openUrl("https://magma-portfolio-sigma.vercel.app") }
                )

                // Instagram Portfolio Button
                CreditMenuButton(
                    title = "Instagram Portfolio",
                    subtitle = "Connect & view visual feeds",
                    iconSymbol = "📸",
                    onClick = { openUrl("https://instagram.com") }
                )

                // Facebook Profile Button
                CreditMenuButton(
                    title = "Facebook Profile",
                    subtitle = "Official social connection",
                    iconSymbol = "📘",
                    onClick = { openUrl("https://facebook.com") }
                )

                // GitHub Repositories Button
                CreditMenuButton(
                    title = "GitHub Repositories",
                    subtitle = "nansari7287-sys / NaeemMusic",
                    iconSymbol = "💻",
                    onClick = { openUrl("https://github.com/nansari7287-sys") }
                )

                // Telegram Network Button
                CreditMenuButton(
                    title = "Telegram Network",
                    subtitle = "Join the developer channel",
                    iconSymbol = "✈️",
                    onClick = { openUrl("https://telegram.org") }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // System Information Section Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF160D2D).copy(alpha = 0.8f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Systems Protocol Info",
                        style = typo().titleMedium,
                        color = Color(0xFF00E5FF),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Location: Chino, Jharkhand, India\n• Framework: Jetpack Compose & Kotlin Multiplatform\n• Security Protocol: Encrypted Build Pipeline via GitHub Actions",
                        style = typo().bodySmall,
                        color = Color(0xFFD1C4E9),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Start,
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Copyright Information
            Text(
                text = "© 2026 Naeem ( DrakoXNaeem ). All rights reserved.",
                style = typo().bodySmall,
                color = Color(0xFFB39DDB),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 8.dp),
                textAlign = TextAlign.Center,
            )

            // Extra bottom spacing for smooth scrolling experience
            Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding() + 180.dp))
        }

        // Custom Glassmorphism Top App Bar
        TopAppBar(
            modifier = Modifier
                .hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.ultraThin(),
                ) {
                    blurEnabled = true
                },
            title = {
                Text(
                    text = "Naeem Music - Credits",
                    style = typo().titleMedium,
                    color = Color.White,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                            animationMode = MarqueeAnimationMode.Immediately,
                        )
                        .focusable(),
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Text(
                        text = "←",
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
                navigationIconContentColor = Color.White,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White,
            ),
        )
    }
}

/**
 * Reusable Custom Menu Button for Links and Social Actions in CreditScreen
 */
@Composable
private fun CreditMenuButton(
    title: String,
    subtitle: String,
    iconSymbol: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF1E1238),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = iconSymbol,
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = typo().bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = typo().bodySmall,
                    color = Color(0xFFB39DDB),
                    fontSize = 12.sp
                )
            }
            Text(
                text = "➔",
                color = Color(0xFF00E5FF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
