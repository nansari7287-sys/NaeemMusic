package com.maxrave.simpmusic.ui.screen.other

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.ui.draw.scale
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
import kotlinx.coroutines.delay

// ==========================================
// 🎨 THEME & PALETTE CONFIGURATION
// ==========================================
val AmoledBlack = Color(0xFF000000)
val NeonPurpleAccent = Color(0xFF9D4EDD)
val NeonGradientBrush = Brush.linearGradient(
    colors = listOf(Color(0xFF00F5FF), Color(0xFF9D4EDD), Color(0xFFFF007F))
)
val CardBackgroundAlpha = Color.Black.copy(alpha = 0.4f)

// ==========================================
// 📊 DATA MODELS
// ==========================================
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
    SocialProfile("Telegram Community", "https://t.me/frexxxy", "telegram"),
    SocialProfile("Official Website", "https://naeem-portfolio-k8sj-ten.vercel.app/", "website")
)

val mediaLinks = listOf(
    SocialProfile("Instagram", "https://www.instagram.com/drakoxnaeem?igsh=MWVrdmh1NXFneDdxNg==", "instagram"),
    SocialProfile("Facebook", "https://www.facebook.com/share/1FsktLSsTn/", "facebook")
)

val coreDevelopmentTeam = listOf(
    TeamMember("𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎", "Lead System Architect & UI Designer", "https://naeem-portfolio-k8sj-ten.vercel.app/", "👑"),
    TeamMember("Naeem", "Creator & Backend Developer", "https://github.com/nansari7287-sys", "⭐")
)

// ==========================================
// 📱 MAIN SCREEN COMPOSABLE
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditScreen(
    paddingValues: PaddingValues, 
    navController: NavController
) {
    val listState = rememberLazyListState()
    var logoTapCount by remember { mutableIntStateOf(0) }
    var isEasterEggActive by remember { mutableStateOf(false) }

    // Easter Egg Logic: 5 baar logo tap karne par unlock hoga
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
        
        // 🎬 BACKGROUND MAGIC (Yeh Android par Video aur PC par Gradient chalayega!)
        DynamicBackground()

        // 🌌 SPARKLING STARS OVERLAY (Premium Effect)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val random = Random(123)
            repeat(60) {
                drawCircle(
                    color = Color.White.copy(alpha = random.nextFloat() * 0.6f + 0.1f),
                    radius = random.nextFloat() * 2.5f,
                    center = Offset(
                        x = random.nextFloat() * size.width, 
                        y = random.nextFloat() * size.height
                    )
                )
            }
        }

        // 📜 MAIN CONTENT UI
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            text = "Developer Hub", 
                            color = Color.White, 
                            fontSize = 18.sp, 
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Text("←", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
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
                        .padding(horizontal = 22.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    // Top Header / Logo Section
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        AnimatedHeaderSection(listState = listState) { logoTapCount++ }
                        Spacer(modifier = Modifier.height(35.dp))
                    }

                    // Community Section
                    item { SectionHeaderTitle("Community Hub") }
                    items(communityLinks) { social -> 
                        ExpandedSocialContactCard(social = social) 
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    // Social Media Section
                    item { SectionHeaderTitle("Media & Socials") }
                    items(mediaLinks) { social -> 
                        ExpandedSocialContactCard(social = social) 
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    // Systems / Developer Section
                    item { SectionHeaderTitle("Systems Info") }
                    items(coreDevelopmentTeam) { dev -> 
                        ExpandedDeveloperCard(developer = dev)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Footer Section
                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                        HorizontalDivider(
                            color = Color.White.copy(alpha = 0.1f), 
                            thickness = 1.dp
                        )
                        Spacer(modifier = Modifier.height(25.dp))
                        ExpandedFooterSection()
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }

                // Easter Egg Popup Overlay
                AnimatedVisibility(
                    visible = isEasterEggActive,
                    enter = fadeIn(animationSpec = tween(400)) + scaleIn(),
                    exit = fadeOut(animationSpec = tween(400)) + scaleOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    SecretEasterEggOverlay { isEasterEggActive = false }
                }
            }
        }
    }
}

// ==========================================
// 🛠️ CUSTOM UI COMPONENTS
// ==========================================

@Composable
fun SectionHeaderTitle(titleText: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = titleText.uppercase(),
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            color = NeonPurpleAccent.copy(alpha = 0.4f), 
            thickness = 1.5.dp
        )
    }
}

@Composable
fun AnimatedHeaderSection(
    listState: LazyListState, 
    onLogoClicked: () -> Unit
) {
    // Scroll aane par opacity kam karne ka logic
    val scrollOffset = if (listState.firstVisibleItemIndex == 0) listState.firstVisibleItemScrollOffset else 400
    val alphaFraction = (1f - (scrollOffset / 400f)).coerceIn(0.1f, 1f)
    
    // Logo ke liye breathing animation (thoda hawa me float karega)
    val infiniteTransition = rememberInfiniteTransition()
    val scaleAnim by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alphaFraction)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .scale(scaleAnim) // Breathing animation applied here
                .clip(CircleShape)
                .background(Color(0xFF1E1035).copy(alpha = 0.8f))
                .border(2.5.dp, NeonGradientBrush, CircleShape)
                .clickable { onLogoClicked() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "N",
                style = TextStyle(
                    brush = NeonGradientBrush,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.ExtraBold,
                    shadow = Shadow(color = NeonPurpleAccent, blurRadius = 30f)
                )
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎",
            style = TextStyle(
                brush = NeonGradientBrush,
                fontSize = 42.sp, 
                fontWeight = FontWeight.Black,
                shadow = Shadow(color = Color(0xFFFF007F), blurRadius = 25f),
                fontStyle = FontStyle.Italic
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "System Architect & UI/UX Developer",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun ExpandedSocialContactCard(social: SocialProfile) {
    val uriHandler = LocalUriHandler.current
    val textBrush = when (social.identifier) {
        "instagram" -> Brush.horizontalGradient(listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFFCA048)))
        "facebook" -> Brush.horizontalGradient(listOf(Color(0xFF00C6FF), Color(0xFF0072FF)))
        "telegram" -> Brush.horizontalGradient(listOf(Color(0xFF28D8FE), Color(0xFF03A9F4)))
        "website" -> Brush.horizontalGradient(listOf(Color(0xFF00F2FE), Color(0xFF4FACFE)))
        else -> SolidColor(Color.White)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { uriHandler.openUri(social.targetUrl) }
            .background(CardBackgroundAlpha)
            .border(0.5.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp, horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomCanvasBrandIcon(type = social.identifier)
        Spacer(modifier = Modifier.width(18.dp))
        Text(
            text = social.platformName, 
            style = TextStyle(brush = textBrush, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text("↗", color = Color.White.copy(alpha = 0.3f), fontSize = 18.sp)
    }
}

@Composable
fun ExpandedDeveloperCard(developer: TeamMember) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { uriHandler.openUri(developer.profileUrl) }
            .background(CardBackgroundAlpha)
            .border(0.5.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp, horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = developer.displaySymbol, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            if (developer.memberName == "𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎" || developer.memberName == "Naeem") {
                Text(
                    text = developer.memberName,
                    style = TextStyle(brush = NeonGradientBrush, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                )
            } else {
                Text(text = developer.memberName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = developer.memberRole, color = Color.LightGray.copy(alpha = 0.8f), fontSize = 13.sp)
        }
    }
}

@Composable
fun CustomCanvasBrandIcon(type: String) {
    Box(
        modifier = Modifier.size(26.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            when (type) {
                "instagram" -> {
                    val instaGradient = Brush.linearGradient(listOf(Color(0xFFF58529), Color(0xFFDD2A7B), Color(0xFF8134AF)))
                    drawRoundRect(brush = instaGradient, size = size, cornerRadius = CornerRadius(8f, 8f), style = Stroke(width = 4f))
                    drawCircle(brush = instaGradient, radius = size.width * 0.25f, style = Stroke(width = 4f))
                    drawCircle(brush = instaGradient, radius = 2f, center = Offset(size.width * 0.75f, size.height * 0.25f))
                }
                "facebook" -> {
                    drawCircle(color = Color(0xFF1877F2), radius = size.width / 2)
                }
                "telegram" -> {
                    drawCircle(color = Color(0xFF2AABEE), radius = size.width / 2)
                }
                "website" -> {
                    drawCircle(brush = NeonGradientBrush, style = Stroke(width = 4f))
                    drawOval(brush = NeonGradientBrush, style = Stroke(width = 2f), size = Size(size.width * 0.4f, size.height), topLeft = Offset(size.width * 0.3f, 0f))
                }
            }
        }
        if (type == "facebook") {
            Text("f", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, fontFamily = FontFamily.Serif, modifier = Modifier.offset(y = (-1).dp))
        } else if (type == "telegram") {
            Text("✈", color = Color.White, fontSize = 14.sp, modifier = Modifier.offset(x = (-1).dp))
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
            text = "SimpMusic Premium",
            style = TextStyle(
                brush = NeonGradientBrush, 
                fontSize = 24.sp, 
                fontWeight = FontWeight.Black, 
                shadow = Shadow(color = NeonPurpleAccent, blurRadius = 15f)
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Engineered by 𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎", 
            style = TextStyle(brush = NeonGradientBrush, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "© 2026 Naeem. All rights reserved.", 
            color = Color.LightGray.copy(alpha = 0.5f), 
            fontSize = 12.sp,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun SecretEasterEggOverlay(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.95f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .border(2.dp, NeonGradientBrush, RoundedCornerShape(20.dp))
                .background(Color(0xFF130B21), RoundedCornerShape(20.dp))
                .padding(40.dp)
        ) {
            Text("⚡👑", fontSize = 80.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎",
                style = TextStyle(
                    brush = NeonGradientBrush, 
                    fontSize = 35.sp, 
                    fontWeight = FontWeight.Black, 
                    shadow = Shadow(Color.Magenta, blurRadius = 40f)
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "You found the Developer's Secret!", 
                color = Color.LightGray, 
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurpleAccent)
            ) {
                Text("Awesome!", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
