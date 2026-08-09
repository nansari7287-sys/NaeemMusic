package com.maxrave.simpmusic.ui.screen.other

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.maxrave.simpmusic.ui.theme.typo
import org.jetbrains.compose.resources.painterResource
import simpmusic.composeapp.generated.resources.Res
import simpmusic.composeapp.generated.resources.app_icon // Placeholder for your future starry background

// --- PREMIUM NEON COLORS ---
val DeepBlackTranslucent = Color(0xAA07030C) // Translucent black so background image shows through
val CardDarkTranslucent = Color(0x88130B21)  // Translucent card background
val NeonPurple = Color(0xFFB388FF)
val NeonPink = Color(0xFFFF4081)

// --- GLOWING NEON TEXT STYLES ---
val neonBrush = Brush.linearGradient(
    colors = listOf(Color(0xFFD580FF), Color(0xFF9D4EDD), Color(0xFFE040FB))
)

val neonGlowStyle = TextStyle(
    brush = neonBrush,
    fontSize = 28.sp,
    shadow = Shadow(color = NeonPurple, blurRadius = 25f), // Asli Neon Glow
    fontWeight = FontWeight.ExtraBold
)

// --- DATA MODEL ---
data class SocialLink(val platform: String, val username: String, val url: String, val type: String)

val mySocials = listOf(
    SocialLink("Instagram", "@drakoxnaeem", "https://www.instagram.com/drakoxnaeem?igsh=MWVrdmh1NXFneDdxNg==", "instagram"),
    SocialLink("Facebook", "DrakoXNaeem", "https://www.facebook.com/share/1FsktLSsTn/", "facebook"),
    SocialLink("Telegram", "@frexxxy", "https://t.me/frexxxy", "telegram"),
    SocialLink("Website", "drakoxnaeem.dev", "https://naeem-portfolio-k8sj-ten.vercel.app/#projects", "website")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditScreen(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        
        // 🌌 BACKGROUND IMAGE LAYER 🌌
        // Yahan 'app_icon' likha hai taaki error na aaye. 
        // Jab tumhare paas stars wala background ho, toh usko project mein add karke yahan uska naam likh dena.
        Image(
            painter = painterResource(Res.drawable.app_icon), 
            contentDescription = "Cosmic Background",
            modifier = Modifier.fillMaxSize().alpha(0.3f), // Alpha kam kiya hai taaki text clear dikhe
            contentScale = ContentScale.Crop
        )
        
        // Dark overlay on top of the image for better readability
        Box(modifier = Modifier.fillMaxSize().background(DeepBlackTranslucent))

        Scaffold(
            containerColor = Color.Transparent, // Transparent so the image shows
            topBar = {
                TopAppBar(
                    title = { 
                        Row {
                            Text("About ", color = Color.Gray, fontSize = 16.sp)
                            Text("𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎", style = TextStyle(brush = neonBrush, fontSize = 16.sp, fontWeight = FontWeight.Bold))
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Text("←", color = Color.White, fontSize = 24.sp)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. GLOWING LOGO SECTION
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    GlowingLogo()
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Professional Neon Name
                    Text(text = "𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎", style = neonGlowStyle)
                    Text(text = "Developer & Creator", color = Color.Gray, fontSize = 14.sp)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 2. STYLISH QUOTE BOX
                item {
                    QuoteBox()
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // 3. CONNECT WITH ME SECTION (WITH CUSTOM CANVAS LOGOS)
                item {
                    Text(
                        text = "Connect with me",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        textAlign = TextAlign.Start
                    )
                }

                items(mySocials) { social ->
                    SocialCard(social)
                }

                // 4. FOOTER
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Powered by ", color = Color.DarkGray, fontSize = 12.sp)
                        Text("𝑵𝒂𝒆𝒆𝒎 𝑴𝒖𝒔𝒊𝒄", style = TextStyle(brush = neonBrush, fontSize = 14.sp, fontWeight = FontWeight.Bold))
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

// --- CUSTOM PROFESSIONAL COMPONENTS ---

@Composable
fun GlowingLogo() {
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(Color(0xFF0F061C), CircleShape)
            .border(2.dp, Brush.sweepGradient(listOf(NeonPurple, NeonPink, NeonPurple)), CircleShape)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "N",
            style = TextStyle(
                brush = neonBrush,
                fontSize = 70.sp,
                shadow = Shadow(color = NeonPurple, blurRadius = 30f),
                fontWeight = FontWeight.ExtraBold
            )
        )
    }
}

@Composable
fun QuoteBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NeonPurple.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .background(CardDarkTranslucent, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "“Code. Create. Inspire.”",
                color = NeonPurple,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Building Amazing Experiences\nfor Music Lovers.",
                color = Color.LightGray,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun SocialCard(social: SocialLink) {
    val uriHandler = LocalUriHandler.current
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(CardDarkTranslucent, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFF2A1B3D), RoundedCornerShape(12.dp))
            .clickable { uriHandler.openUri(social.url) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomBrandIcon(type = social.type)
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(text = social.platform, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(text = social.username, color = Color.Gray, fontSize = 12.sp)
        }
        
        Text(text = ">", color = NeonPurple, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CustomBrandIcon(type: String) {
    Box(
        modifier = Modifier.size(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            when (type) {
                "instagram" -> {
                    val gradient = Brush.linearGradient(listOf(Color(0xFFF58529), Color(0xFFDD2A7B), Color(0xFF8134AF)))
                    drawRoundRect(brush = gradient, size = size, cornerRadius = CornerRadius(10f, 10f), style = Stroke(width = 4f))
                    drawCircle(brush = gradient, radius = size.width * 0.25f, style = Stroke(width = 4f))
                    drawCircle(brush = gradient, radius = 2f, center = Offset(size.width * 0.75f, size.height * 0.25f))
                }
                "facebook" -> {
                    drawCircle(color = Color(0xFF1877F2))
                }
                "telegram" -> {
                    drawCircle(color = Color(0xFF2AABEE))
                }
                "website" -> {
                    drawCircle(color = Color(0xFF9D4EDD), style = Stroke(width = 4f))
                    drawOval(color = Color(0xFF9D4EDD), style = Stroke(width = 2f), size = Size(size.width * 0.4f, size.height), topLeft = Offset(size.width * 0.3f, 0f))
                    drawLine(color = Color(0xFF9D4EDD), start = Offset(0f, size.height / 2), end = Offset(size.width, size.height / 2), strokeWidth = 2f)
                }
            }
        }
        
        if (type == "facebook") {
            Text("f", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, fontFamily = FontFamily.Serif, modifier = Modifier.offset(y = (-2).dp))
        } else if (type == "telegram") {
            Text("✈", color = Color.White, fontSize = 16.sp, modifier = Modifier.offset(x = (-1).dp, y = (-1).dp))
        }
    }
}
