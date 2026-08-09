package com.maxrave.simpmusic.ui.screen.other

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.maxrave.simpmusic.expect.openUrl
import com.maxrave.simpmusic.ui.theme.typo

// --- HARDCODED THEME COLORS ---
val amoledBlack = Color(0xFF000000)
val neonPurple = Color(0xFF9D4EDD)
val darkSurface = Color(0xFF120A1A)

// --- DATA STRUCTURES ---
data class DeveloperProfile(val name: String, val role: String, val url: String, val symbol: String)
data class OpenSourceLibrary(val name: String, val author: String, val license: String, val url: String)

val developerTeam = listOf(
    DeveloperProfile("DrakoXNaeem", "Lead System Architect & Developer", "https://magma-portfolio-sigma.vercel.app", "👑"),
    DeveloperProfile("Maxrave", "Original SimpMusic Base Creator", "https://github.com/maxrave-dev", "⭐")
)

val usedLibraries = listOf(
    OpenSourceLibrary("Compose Multiplatform", "JetBrains", "Apache 2.0", "https://www.jetbrains.com/lp/compose-multiplatform/"),
    OpenSourceLibrary("Haze", "Chris Banes", "Apache 2.0", "https://github.com/chrisbanes/haze"),
    OpenSourceLibrary("Ktor", "JetBrains", "Apache 2.0", "https://ktor.io/"),
    OpenSourceLibrary("Coil", "Colin White", "Apache 2.0", "https://coil-kt.github.io/coil/")
)

// --- MAIN SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditScreen(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    val listState = rememberLazyListState()
    var logoClicks by remember { mutableIntStateOf(0) }
    var showEasterEgg by remember { mutableStateOf(false) }

    if (logoClicks >= 5) {
        showEasterEgg = true
        logoClicks = 0
    }

    Scaffold(
        containerColor = amoledBlack,
        topBar = {
            TopAppBar(
                title = { Text("Developer Hub", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = amoledBlack)
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // 1. DYNAMIC HEADER
                item {
                    DynamicAnimatedHeader(listState = listState) { logoClicks++ }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                // 2. DEVELOPER TEAM
                item {
                    SectionTitle("Core Team", Icons.Default.Person)
                }
                items(developerTeam) { developer ->
                    DeveloperCard(developer)
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                // 3. SETTINGS & PREFERENCES
                item {
                    SectionTitle("App Preferences", Icons.Default.Settings)
                    AdvancedSettingsPanel()
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                // 4. LIBRARIES
                item {
                    SectionTitle("Open Source", Icons.Default.Info)
                }
                items(usedLibraries) { library ->
                    LibraryCard(library)
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
                item { FooterCredits() }
            }

            // EASTER EGG OVERLAY
            AnimatedVisibility(
                visible = showEasterEgg,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                EasterEggOverlay { showEasterEgg = false }
            }
        }
    }
}

// --- MODULAR COMPONENTS ---
@Composable
fun SectionTitle(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Icon(icon, contentDescription = null, tint = neonPurple, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, color = Color.White, style = typo().titleMedium.copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
fun DynamicAnimatedHeader(
    listState: androidx.compose.foundation.lazy.LazyListState, 
    onLogoClick: () -> Unit
) {
    val scrollOffset = if (listState.firstVisibleItemIndex == 0) listState.firstVisibleItemScrollOffset else 400
    val scrollFraction = (scrollOffset / 400f).coerceIn(0f, 1f)
    val contentAlpha = 1f - (scrollFraction * 1.5f).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(darkSurface),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.alpha(contentAlpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(neonPurple.copy(alpha = 0.2f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onLogoClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("N", color = neonPurple, fontSize = 48.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("DrakoXNaeem", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("Lead Architect & Visionary", color = neonPurple, fontSize = 14.sp)
        }
    }
}

@Composable
fun DeveloperCard(developer: DeveloperProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { openUrl(developer.url) },
        colors = CardDefaults.cardColors(containerColor = darkSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = developer.symbol, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = developer.name, color = Color.White, fontWeight = FontWeight.Bold)
                Text(text = developer.role, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun AdvancedSettingsPanel() {
    var highQualityAudio by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(darkSurface)
            .border(1.dp, neonPurple.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Extreme Audio Quality", color = Color.White, fontWeight = FontWeight.Medium)
                Text("Force 320kbps streaming", color = Color.Gray, fontSize = 12.sp)
            }
            Switch(
                checked = highQualityAudio,
                onCheckedChange = { highQualityAudio = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = neonPurple, 
                    checkedTrackColor = neonPurple.copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Composable
fun LibraryCard(library: OpenSourceLibrary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { openUrl(library.url) }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = library.name, color = Color.LightGray, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(text = "By ${library.author}", color = Color.Gray, fontSize = 12.sp)
        }
        Text(text = library.license, color = neonPurple.copy(alpha = 0.8f), fontSize = 12.sp)
    }
}

@Composable
fun FooterCredits() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Powered by Compose Multiplatform", color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text("NaeemMusic Advanced Build v2.0", color = neonPurple, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun EasterEggOverlay(onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f))
            .clickable(onClick = onClose),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Star, contentDescription = null, tint = neonPurple, modifier = Modifier.size(80.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("You found the secret!", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Stay Awesome 🚀", color = neonPurple, fontSize = 18.sp)
        }
    }
}
