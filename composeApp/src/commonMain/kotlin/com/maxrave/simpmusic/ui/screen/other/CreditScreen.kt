package com.maxrave.simpmusic.ui.screen.other

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.random.Random

// --- 🎨 NEON THEME & PALETTE CONFIGURATION ---
val AmoledBlack = Color(0xFF000000)
val SpaceCardColor = Color(0xFF130B21)
val NeonPurpleAccent = Color(0xFF9D4EDD)
val NeonGradientBrush = Brush.linearGradient(
    colors = listOf(Color(0xFF00F5FF), Color(0xFF9D4EDD), Color(0xFFFF007F))
)
val SpaceRadialGradient = Brush.radialGradient(
    colors = listOf(Color(0xFF2D1B4E), Color(0xFF07030C))
)

// --- 📊 DATA MODELS FOR SOCIALS & DEVELOPERS ---
data class SocialProfile(
    val platformName: String,
    val targetUrl: String,
    val identifier: String
)

data class TeamMember(
    val memberName: String,
    val memberRole: String,
    val profileUrl: String,
    val displaySymbol: String
)

val communityLinks = listOf(
    SocialProfile("Telegram Network", "https://t.me/frexxxy", "telegram"),
    SocialProfile("Portfolio Website", "https://naeem-portfolio-k8sj-ten.vercel.app/", "website")
)

val mediaLinks = listOf(
    SocialProfile("Instagram Portfolio", "https://www.instagram.com/drakoxnaeem?igsh=MWVrdmh1NXFneDdxNg==", "instagram"),
    SocialProfile("Facebook Profile", "https://www.facebook.com/share/1FsktLSsTn/", "facebook")
)

val coreDevelopmentTeam = listOf(
    TeamMember("𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎", "Lead System Architect", "https://naeem-portfolio-k8sj-ten.vercel.app/", "👑"),
    TeamMember("Maxrave", "Original Base Creator", "https://github.com/maxrave-dev", "⭐")
)

// --- 📱 MAIN CREDIT SCREEN COMPOSABLE ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditScreen(
    paddingValues: PaddingValues,
    navController: NavController
) {
    val listState = rememberLazyListState()
    var logoTapCount by remember { mutableIntStateOf(0) }
    var isEasterEggActive by remember { mutableStateOf(false) }

    if (logoTapCount >= 5) {
        isEasterEggActive = true
        logoTapCount = 0
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(AmoledBlack)
    ) {
        // 🌌 Procedural Starry Space Background Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(brush = SpaceRadialGradient)
            repeat(70) {
                drawCircle(
                    color = Color.White.copy(alpha = Random.nextFloat() * 0.8f + 0.2f),
                    radius = Random.nextFloat() * 2.8f,
                    center = Offset(Random.nextFloat() * size.width, Random.nextFloat() * size.height)
                )
            }
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text("Developer Hub", color = Color.LightGray, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Text("←", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 20.dp),
                    contentPadding = PaddingValues(bottom = 60.dp)
                ) {
                    // 1. Dynamic Animated Header
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        HeaderSection(listState = listState) { logoTapCount++ }
                        Spacer(modifier = Modifier.height(30.dp))
                    }

                    // 2. COMMUNITY HUB Section
                    item {
                        SectionHeaderTitle("Community Hub")
                    }
                    items(communityLinks) { social ->
                        ExpandedSocialContactCard(social = social)
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    // 3. MEDIA & SOCIALS Section
                    item {
                        SectionHeaderTitle("Media & Socials")
                    }
                    items(mediaLinks) { social ->
                        ExpandedSocialContactCard(social = social)
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    // 4. SYSTEMS INFO Section
                    item {
                        SectionHeaderTitle("Systems Info")
                    }
                    items(coreDevelopmentTeam) { dev ->
                        ExpandedDeveloperCard(developer = dev)
                    }

                    // 5. Footer Credits
                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                        HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.3f), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(20.dp))
                        ExpandedFooterSection()
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }

                // Hidden Easter Egg Overlay
                AnimatedVisibility(
                    visible = isEasterEggActive,
                    enter = fadeIn(animationSpec = tween(400)),
                    exit = fadeOut(animationSpec = tween(400))
                ) {
                    SecretEasterEggOverlay { isEasterEggActive = false }
                }
            }
        }
    }
}

// --- 🛠️ HELPER COMPONENTS ---

@Composable
fun SectionHeaderTitle(titleText: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = titleText.uppercase(),
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.4f), thickness = 1.dp)
    }
}

@Composable
fun HeaderSection(
    listState: androidx.compose.foundation.lazy.LazyListState,
    onLogoClicked: () -> Unit
) {
    val scrollOffset = if (listState.firstVisibleItemIndex == 0) listState.firstVisibleItemScrollOffset else 300
    val alphaFraction = (1f - (scrollOffset / 300f)).coerceIn(0.2f, 1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alphaFraction)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color(0xFF1E1035))
                .border(2.dp, NeonGradientBrush, CircleShape)
                .clickable { onLogoClicked() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "N",
                style = TextStyle(
                    brush = NeonGradientBrush,
                    fontSize = 45.sp,
                    fontWeight = FontWeight.ExtraBold,
                    shadow = Shadow(color = NeonPurpleAccent, blurRadius = 25f)
                )
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎",
            style = TextStyle(
                brush = NeonGradientBrush,
                fontSize = 38.sp, // Made significantly bigger
                fontWeight = FontWeight.ExtraBold,
                shadow = Shadow(color = Color.Magenta, blurRadius = 20f),
                fontStyle = FontStyle.Italic
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "System Architect & Developer",
            color = Color.LightGray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ExpandedSocialContactCard(social: SocialProfile) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { uriHandler.openUri(social.targetUrl) }
            .padding(vertical = 14.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomCanvasBrandIcon(type = social.identifier)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = social.platformName, 
            color = Color(0xFFE2E8F0), 
            fontSize = 15.sp, 
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ExpandedDeveloperCard(developer: TeamMember) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { uriHandler.openUri(developer.profileUrl) }
            .padding(vertical = 14.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = ">", color = NeonPurpleAccent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            if (developer.memberName == "𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎") {
                Text(
                    text = "𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎",
                    style = TextStyle(
                        brush = NeonGradientBrush,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            } else {
                Text(text = developer.memberName, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = developer.memberRole, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun CustomCanvasBrandIcon(type: String) {
    Box(
        modifier = Modifier.size(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            when (type) {
                "instagram" -> {
                    val instaGradient = Brush.linearGradient(listOf(Color(0xFFF58529), Color(0xFFDD2A7B), Color(0xFF8134AF)))
                    drawRoundRect(brush = instaGradient, size = size, cornerRadius = CornerRadius(6f, 6f), style = Stroke(width = 3.5f))
                    drawCircle(brush = instaGradient, radius = size.width * 0.25f, style = Stroke(width = 3.5f))
                    drawCircle(brush = instaGradient, radius = 1.5f, center = Offset(size.width * 0.75f, size.height * 0.25f))
                }
                "facebook" -> {
                    drawCircle(color = Color(0xFF1877F2), radius = size.width / 2)
                }
                "telegram" -> {
                    drawCircle(color = Color(0xFF2AABEE), radius = size.width / 2)
                }
                "website" -> {
                    drawCircle(brush = NeonGradientBrush, style = Stroke(width = 3f))
                    drawOval(brush = NeonGradientBrush, style = Stroke(width = 1.5f), size = Size(size.width * 0.4f, size.height), topLeft = Offset(size.width * 0.3f, 0f))
                }
            }
        }
        if (type == "facebook") {
            Text("f", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, fontFamily = FontFamily.Serif, modifier = Modifier.offset(y = (-1).dp))
        } else if (type == "telegram") {
            Text("✈", color = Color.White, fontSize = 12.sp, modifier = Modifier.offset(x = (-1).dp))
        }
    }
}

@Composable
fun ExpandedFooterSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Naeem Music",
            style = TextStyle(
                brush = NeonGradientBrush, 
                fontSize = 22.sp, 
                fontWeight = FontWeight.ExtraBold,
                shadow = Shadow(color = NeonPurpleAccent, blurRadius = 10f)
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        // Footer mein sub-branding ko bhi bada kiya gaya hai
        Text(
            text = "𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎", 
            style = TextStyle(
                brush = NeonGradientBrush, 
                fontSize = 16.sp, 
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text("© 2026 Naeem (DrakoXNaeem). All rights reserved.", color = Color.Gray, fontSize = 11.sp)
    }
}

@Composable
fun SecretEasterEggOverlay(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.92f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("⚡👑", fontSize = 70.sp)
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎",
                style = TextStyle(
                    brush = NeonGradientBrush,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    shadow = Shadow(Color.Magenta, blurRadius = 30f)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Hidden Developer Overlay Unlocked 🚀", color = Color.LightGray, fontSize = 15.sp)
        }
    }
}
