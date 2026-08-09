package com.maxrave.simpmusic.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maxrave.domain.extension.now
import com.maxrave.simpmusic.ui.theme.typo
import com.maxrave.simpmusic.utils.VersionManager
import org.jetbrains.compose.resources.stringResource
import simpmusic.composeapp.generated.resources.Res
import simpmusic.composeapp.generated.resources.app_name
import simpmusic.composeapp.generated.resources.version_format

@Composable
fun EndOfPage(withoutCredit: Boolean = false) {
    // 🔗 UriHandler ka use links ko browser mein open karne ke liye hota hai
    val uriHandler = LocalUriHandler.current

    // 🎨 Neon colors for your name (Purple to Cyan gradient)
    val neonColors = listOf(Color(0xFFB026FF), Color(0xFF00D4FF))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        if (!withoutCredit) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 20.dp).alpha(0.8f)
            ) {
                // 📱 App Name and Version
                Text(
                    "@${now().year} " + stringResource(Res.string.app_name) + " " +
                        stringResource(
                            Res.string.version_format,
                            VersionManager.getVersionName(),
                        ),
                    style = typo().bodySmall,
                    textAlign = TextAlign.Center,
                )
                
                // 👤 Developer Text (Neon & Bold)
                Text(
                    text = "Developed by 𝑵𝒂𝒆𝒆𝒎",
                    style = typo().bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .graphicsLayer(alpha = 0.99f)
                        .drawWithCache {
                            val brush = Brush.linearGradient(neonColors)
                            onDrawWithContent {
                                drawContent()
                                drawRect(brush, blendMode = BlendMode.SrcAtop)
                            }
                        }
                )

                // 🌐 Clickable Social Links Row (With Colorful Icons)
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    
                    // Instagram
                    Row(
                        modifier = Modifier.clickable { uriHandler.openUri("https://www.instagram.com/drakoxnaeem?igsh=MWVrdmh1NXFneDdxNg==") },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("📸 ", style = typo().bodyMedium)
                        Text("Instagram", style = typo().bodySmall)
                    }

                    // Facebook
                    Row(
                        modifier = Modifier.clickable { uriHandler.openUri("https://www.facebook.com/share/1FsktLSsTn/") },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("📘 ", style = typo().bodyMedium)
                        Text("Facebook", style = typo().bodySmall)
                    }

                    // Telegram
                    Row(
                        modifier = Modifier.clickable { uriHandler.openUri("https://t.me/frexxxy") },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("✈️ ", style = typo().bodyMedium)
                        Text("Telegram", style = typo().bodySmall)
                    }

                    // Website
                    Row(
                        modifier = Modifier.clickable { uriHandler.openUri("https://naeem-portfolio-k8sj-ten.vercel.app/#projects") },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🌐 ", style = typo().bodyMedium)
                        Text("Website", style = typo().bodySmall)
                    }
                }
            }
        }
    }
}
