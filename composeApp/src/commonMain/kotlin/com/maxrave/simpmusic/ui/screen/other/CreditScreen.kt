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
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding() + 94.dp))

            Image(
                painter = painterResource(Res.drawable.app_icon),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Naeem Music",
                style = typo().titleLarge,
                fontSize = 24.sp,
            )

            Text(
                text = stringResource(
                    Res.string.version_format,
                    VersionManager.getVersionName(),
                ),
                style = typo().bodySmall,
                fontSize = 13.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Developed by DrakoXNaeem",
                style = typo().bodyMedium,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    openUrl("https://magma-portfolio-sigma.vercel.app")
                },
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "A professional cross-platform music streaming experience.",
                style = typo().bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                textAlign = TextAlign.Start,
            )

            Spacer(modifier = Modifier.height(10.dp))

            CompositionLocalProvider(
                LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
            ) {
                TextButton(
                    onClick = { openUrl("https://magma-portfolio-sigma.vercel.app") },
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 25.dp)
                        .defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                ) {
                    Text(text = "🌐 Official Website")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "© 2026 Naeem. All rights reserved.",
                style = typo().bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 5.dp),
                textAlign = TextAlign.Start,
            )

            Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding() + 200.dp))
        }

        TopAppBar(
            modifier = Modifier.hazeEffect(
                state = hazeState,
                style = HazeMaterials.ultraThin(),
            ) {
                blurEnabled = true
            },
            title = {
                Text(
                    text = "Naeem Music",
                    style = typo().titleMedium,
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
                        fontSize = 22.sp,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Unspecified,
                navigationIconContentColor = Color.Unspecified,
                titleContentColor = Color.Unspecified,
                actionIconContentColor = Color.Unspecified,
            ),
        )
    }
}
