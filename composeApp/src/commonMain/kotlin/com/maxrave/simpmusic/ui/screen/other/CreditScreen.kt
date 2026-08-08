package com.maxrave.simpmusic.ui.screen.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.maxrave.simpmusic.expect.openUrl
import com.maxrave.simpmusic.ui.component.RippleIconButton
import com.maxrave.simpmusic.ui.icon.SimpIcons
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun CreditScreen(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    val hazeState = rememberHazeState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 64.dp)
                .verticalScroll(rememberScrollState())
                .hazeSource(state = hazeState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        // App Icon
        Image(
            painter = painterResource(Res.drawable.app_icon),
            contentDescription = "Naeem Music App Icon",
            modifier =
                Modifier
                    .size(150.dp)
                    .clip(CircleShape),
        )

        Spacer(modifier = Modifier.height(25.dp))

        // App Name
        Text(
            text = stringResource(Res.string.app_name),
            style = typo().titleLarge,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Version
        Text(
            text = stringResource(
                Res.string.version_format,
                VersionManager.getVersionName(),
            ),
            style = typo().bodySmall,
            fontSize = 13.sp,
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Creator
        Text(
            text = "𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎",
            style = typo().bodyMedium,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
            modifier =
                Modifier.clickable {
                    openUrl("https://www.instagram.com/drakoxnaeem")
                },
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Official Creator & Developer",
            style = typo().bodySmall,
            fontSize = 12.sp,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // About / Description
        Text(
            text = "𝑵𝒂𝒆𝒆𝒎 𝑴𝒖𝒔𝒊𝒄",
            style = typo().titleMedium,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
            textAlign = TextAlign.Start,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text =
                "A modern music application designed for a clean, "
                    + "fast and immersive listening experience.\n\n"
                    + "Built and customized under the "
                    + "𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎 brand.",
            style = typo().bodyMedium,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
            textAlign = TextAlign.Start,
        )

        Spacer(modifier = Modifier.height(20.dp))

        CompositionLocalProvider(
            LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
        ) {

            // Instagram
            TextButton(
                onClick = {
                    openUrl("https://www.instagram.com/drakoxnaeem")
                },
                modifier =
                    Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 25.dp)
                        .defaultMinSize(
                            minHeight = 1.dp,
                            minWidth = 1.dp,
                        ),
            ) {
                Column {
                    Text(
                        text = "Instagram",
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "@drakoxnaeem",
                        style = typo().bodySmall,
                    )
                }
            }

            // Facebook
            TextButton(
                onClick = {
                    openUrl("https://www.facebook.com/share/1986k9AkPX/")
                },
                modifier =
                    Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 25.dp)
                        .defaultMinSize(
                            minHeight = 1.dp,
                            minWidth = 1.dp,
                        ),
            ) {
                Column {
                    Text(
                        text = "Facebook",
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "𝑫𝒓𝒂𝒌𝒐𝑿𝑵𝒂𝒆𝒆𝒎",
                        style = typo().bodySmall,
                    )
                }
            }

            // Email
            TextButton(
                onClick = {
                    openUrl("mailto:nansari7287@gmail.com")
                },
                modifier =
                    Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 25.dp)
                        .defaultMinSize(
                            minHeight = 1.dp,
                            minWidth = 1.dp,
                        ),
            ) {
                Column {
                    Text(
                        text = "Email",
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "nansari7287@gmail.com",
                        style = typo().bodySmall,
                    )
                }
            }

            // GitHub / Open-source project
            TextButton(
                onClick = {
                    openUrl("https://github.com/nansari7287-sys/NaeemMusic")
                },
                modifier =
                    Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 25.dp)
                        .defaultMinSize(
                            minHeight = 1.dp,
                            minWidth = 1.dp,
                        ),
            ) {
                Column {
                    Text(
                        text = "Naeem Music GitHub",
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Source code & project updates",
                        style = typo().bodySmall,
                    )
                }
            }

            // Original project credits
            TextButton(
                onClick = {
                    openUrl("https://github.com/maxrave-dev/SimpMusic")
                },
                modifier =
                    Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 25.dp)
                        .defaultMinSize(
                            minHeight = 1.dp,
                            minWidth = 1.dp,
                        ),
            ) {
                Column {
                    Text(
                        text = "Open-source Credits",
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Original SimpMusic project",
                        style = typo().bodySmall,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Copyright
        Text(
            text = "©2026 𝑵𝒂𝒆𝒆𝒎 𝑴𝒖𝒔𝒊𝒄",
            style = typo().bodySmall,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 5.dp),
            textAlign = TextAlign.Start,
        )

        Spacer(modifier = Modifier.height(200.dp))
    }

    // Top App Bar
    TopAppBar(
        modifier =
            Modifier
                .hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.ultraThin(),
                ) {
                    blurEnabled = true
                },
        title = {
            Text(
                text = stringResource(Res.string.app_name),
                style = typo().titleMedium,
                maxLines = 1,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(
                            align = Alignment.CenterVertically,
                        )
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                            animationMode =
                                MarqueeAnimationMode.Immediately,
                        )
                        .focusable(),
            )
        },
        navigationIcon = {
            Box(
                Modifier.padding(horizontal = 5.dp),
            ) {
                RippleIconButton(
                    SimpIcons.ArrowBackIosNew,
                    Modifier.size(32.dp),
                    true,
                    tint = MaterialTheme.colorScheme.onSurface,
                ) {
                    navController.navigateUp()
                }
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                Color.Transparent,
                Color.Unspecified,
                Color.Unspecified,
                Color.Unspecified,
                Color.Unspecified,
            ),
    )
}