package com.maxrave.simpmusic.ui.screen.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
// Naye Imports jo back button ke liye zaroori hain
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
// Baaki aapke purane imports
import androidx.compose.material3.ExperimentalMaterial3Api
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

        // App icon
        Image(
            painter = painterResource(Res.drawable.app_icon),
            contentDescription = "App Icon",
            modifier =
                Modifier
                    .size(150.dp)
                    .clip(CircleShape),
        )

        Spacer(modifier = Modifier.height(30.dp))

        // App name
        Text(
            text = stringResource(Res.string.app_name),
            style = typo().titleLarge,
            fontSize = 22.sp,
        )

        // Version
        Text(
            text =
                stringResource(
                    Res.string.version_format,
                    VersionManager.getVersionName(),
                ),
            style = typo().bodySmall,
            fontSize = 13.sp,
        )

        // Developer
        Text(
            text = stringResource(Res.string.maxrave_dev),
            style = typo().bodyMedium,
            textDecoration = TextDecoration.Underline,
            modifier =
                Modifier.clickable {
                    openUrl("https://magma-portfolio-sigma.vercel.app")
                },
        )

        Spacer(modifier = Modifier.height(20.dp))

        // App description
        Text(
            text = stringResource(Res.string.credit_app),
            style = typo().bodyMedium,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
            textAlign = TextAlign.Start,
        )

        Spacer(modifier = Modifier.height(10.dp))

        CompositionLocalProvider(
            LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
        ) {
            // Website button
            TextButton(
                onClick = {
                    openUrl("https://magma-portfolio-sigma.vercel.app")
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
                Text(
                    text = stringResource(Res.string.website),
                )
            }

            // Developer blog button
            TextButton(
                onClick = {
                    openUrl("https://github.com/themagmalord333-oss")
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
                        text = stringResource(Res.string.developer_blog),
                    )

                    Text(
                        text =
                            stringResource(
                                Res.string.developer_blog_tagline,
                            ),
                        style = typo().bodySmall,
                    )
                }
            }

            // GitHub button
            TextButton(
                onClick = {
                    openUrl("https://Aapke code ko maine dhyan se check kiya hai. Code ka structure accha hai, par isme kuch **logical aur UI issues** the, khaas karke `Haze` (glassmorphism/blur) effect aur `Navigation` ko lekar. 

Maine unhe sahi kar diya hai. Yahan main batata hoon ki kya galat tha aur usko kaise fix kiya gaya hai:

### **Main Issues Jinko Fix Kiya Gaya Hai:**
1. **Missing `Box` Layout:** Aapne `Column` aur `TopAppBar` ko ek saath bina kisi parent container (jaise `Box`) ke rakha tha. Compose mein properly over-lap karne ke liye dono ko ek `Box` mein wrap karna zaroori hai.
2. **Haze Blur Effect Kaam Nahi Kar Raha Tha:** Aapne `Modifier.padding` ko `verticalScroll` se pehle lagaya tha. Iski wajah se list pehle hi cut jati thi aur `TopAppBar` ke peeche scroll nahi hoti thi (isliye blur effect dikhta hi nahi). Maine padding ko hata kar andar `Spacer` use kiya hai, taaki list `TopAppBar` ke peeche scroll ho aur blur sahi se dikhe.
3. **Missing Back Button:** Aapne `navController` pass toh kiya tha function mein, par back aane ke liye koi button nahi banaya tha. Maine `TopAppBar` mein `navigationIcon` add kar diya hai jisse back nav kaam karega.
4. **Duplicate URLs:** Aapke sabhi buttons (Developer Blog, GitHub, Issue Tracker, Buy me a coffee) mein ek hi URL (`"https://github.com/themagmalord333-oss"`) copy-paste ho gaya hai. *Maine unhe waise hi rakha hai, par aap unhe apne real links ke sath zaroor update kar lijiyega.*

### **Sahi Kiya Hua Code (Corrected `CreditScreen.kt`):**

```kotlin
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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

    // Ek Box use karna zaroori hai taaki TopAppBar Column ke upar overlap kare
    Box(modifier = Modifier.fillMaxSize()) {
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState) // Haze source pehle apply hoga
                .verticalScroll(rememberScrollState()), // Padding scroll ke baad nahi honi chahiye
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Padding ko Spacers se handle kiya gaya hai taaki content TopAppBar ke peeche se nikle aur blur ho
            Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding() + 94.dp)) // 64dp AppBar + 30dp space

            // App icon
            Image(
                painter = painterResource(Res.drawable.app_icon),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
            )

            Spacer(modifier = Modifier.height(30.dp))

            // App name
            Text(
                text = stringResource(Res.string.app_name),
                style = typo().titleLarge,
                fontSize = 22.sp,
            )

            // Version
            Text(
                text = stringResource(
                    Res.string.version_format,
                    VersionManager.getVersionName(),
                ),
                style = typo().bodySmall,
                fontSize = 13.sp,
            )

            // Developer
            Text(
                text = stringResource(Res.string.maxrave_dev),
                style = typo().bodyMedium,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    openUrl("[https://magma-portfolio-sigma.vercel.app](https://magma-portfolio-sigma.vercel.app)")
                },
            )

            Spacer(modifier = Modifier.height(20.dp))

            // App description
            Text(
                text = stringResource(Res.string.credit_app),
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
                // Website button
                TextButton(
                    onClick = {
                        openUrl("[https://magma-portfolio-sigma.vercel.app](https://magma-portfolio-sigma.vercel.app)")
                    },
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 25.dp)
                        .defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                ) {
                    Text(text = stringResource(Res.string.website))
                }

                // Developer blog button
                TextButton(
                    onClick = { openUrl("[https://github.com/themagmalord333-oss](https://github.com/themagmalord333-oss)") }, // UPDATE THIS URL
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 25.dp)
                        .defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                ) {
                    Column {
                        Text(text = stringResource(Res.string.developer_blog))
                        Text(
                            text = stringResource(Res.string.developer_blog_tagline),
                            style = typo().bodySmall,
                        )
                    }
                }

                // GitHub button
                TextButton(
                    onClick = { openUrl("[https://github.com/themagmalord333-oss](https://github.com/themagmalord333-oss)") },
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 25.dp)
                        .defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                ) {
                    Text(text = stringResource(Res.string.github))
                }

                // Issue tracker button
                TextButton(
                    onClick = { openUrl("[https://github.com/themagmalord333-oss](https://github.com/themagmalord333-oss)") }, // UPDATE THIS URL
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 25.dp)
                        .defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                ) {
                    Text(text = stringResource(Res.string.issue_tracker))
                }

                // Buy me a coffee button
                TextButton(
                    onClick = { openUrl("[https://github.com/themagmalord333-oss](https://github.com/themagmalord333-oss)") }, // UPDATE THIS URL
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 25.dp)
                        .defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                ) {
                    Text(text = stringResource(Res.string.buy_me_a_coffee))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Copyright
            Text(
                text = stringResource(Res.string.copyright),
                style = typo().bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 5.dp),
                textAlign = TextAlign.Start,
            )

            // Bottom padding insets bhi include karein
            Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding() + 200.dp))
        }

        // TopAppBar Box ke andar list ke upar place hoga
        TopAppBar(
            modifier = Modifier.hazeEffect(
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
                // Back Button add kiya gaya taaki user screen close kar sake
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent, // Sirf containerColor Transparent chahiye
                scrolledContainerColor = Color.Unspecified,
                navigationIconContentColor = Color.Unspecified,
                titleContentColor = Color.Unspecified,
                actionIconContentColor = Color.Unspecified,
            ),
        )
    }
}
