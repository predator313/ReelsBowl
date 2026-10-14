package com.nfs.reelsbowl.login.presentation

import androidx.activity.compose.LocalActivity
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nfs.reelsbowl.R
import com.nfs.reelsbowl.login.domain.AuthUiState
import com.nfs.reelsbowl.ui.theme.LegalTextStyle
import com.nfs.reelsbowl.ui.theme.ReelsBowlTheme

@Composable
fun LoginRoute(
    onOpenTerms: () -> Unit,
    onOpenPrivacy: () -> Unit,
    onContinueAsGuest: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // Credential Manager needs an Activity to show the Google account picker.
    val activity = LocalActivity.current

    LoginScreen(
        uiState = uiState,
        onContinueWithGoogle = { activity?.let(viewModel::signInWithGoogle) },
        onContinueAsGuest = onContinueAsGuest,
        onOpenTerms = onOpenTerms,
        onOpenPrivacy = onOpenPrivacy,
        onErrorShown = viewModel::onErrorShown,
        modifier = modifier,
    )
}

@Composable
private fun LoginScreen(
    uiState: AuthUiState,
    onContinueWithGoogle: () -> Unit,
    onContinueAsGuest: () -> Unit,
    onOpenTerms: () -> Unit,
    onOpenPrivacy: () -> Unit,
    onErrorShown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val errorMessage = uiState.errorMessage
    val errorText = errorMessage?.let { stringResource(it) }

    LaunchedEffect(errorMessage) {
        if (errorText != null) {
            snackBarHostState.showSnackbar(errorText)
            onErrorShown()
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { contentPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .ambientGlow(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    // Keeps SpaceBetween meaningful on tall screens while still scrolling on short ones.
                    .heightIn(min = maxHeight)
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                StatusPill()
                HeroSection()
                AuthenticationSection(
                    isSigningIn = uiState.isSigningIn,
                    onContinueWithGoogle = onContinueWithGoogle,
                    onContinueAsGuest = onContinueAsGuest,
                    onOpenTerms = onOpenTerms,
                    onOpenPrivacy = onOpenPrivacy,
                )
            }
        }
    }
}

// region Hero

@Composable
private fun StatusPill(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(ReelsBowlTheme.brand.surfacePill.copy(alpha = 0.7f))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(8.dp)
                .background(ReelsBowlTheme.brand.accentCyan, CircleShape)
        )
        Text(
            text = stringResource(R.string.login_status_pill),
            style = MaterialTheme.typography.labelMedium,
            color = ReelsBowlTheme.brand.textSecondary,
        )
    }
}

@Composable
private fun HeroSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BrandEmblem()
        Spacer(Modifier.height(24.dp))
        BrandWordmark()
        Spacer(Modifier.height(8.dp))
        BrandPillars()
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.login_tagline),
            style = MaterialTheme.typography.bodyMedium,
            color = ReelsBowlTheme.brand.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier,
        )
        Spacer(Modifier.height(24.dp))
        FeatureBadges()
    }
}

@Composable
private fun BrandEmblem(modifier: Modifier = Modifier) {
    val brand = ReelsBowlTheme.brand
    Box(
        modifier = modifier.size(EmblemHaloSize),
        contentAlignment = Alignment.Center
    ) {
        // Neon halo. A radial gradient rather than Modifier.blur, which is ignored below API 31.
        Box(
            Modifier
                .fillMaxSize()
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                brand.accentPink.copy(alpha = 0.45f),
                                brand.accentCyan.copy(alpha = 0.22f),
                                Color.Transparent,
                            ),
                            center = center,
                            radius = size.minDimension / 2f,
                        )
                    )
                }
        )
        Box(
            modifier = Modifier
                .size(112.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = brand.accentPink,
                    spotColor = brand.accentPink,
                )
                .clip(RoundedCornerShape(24.dp))
                .background(brand.surfaceEmblem.copy(alpha = 0.9f))
                .padding(12.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.reelbowl_logo),
                contentDescription = stringResource(R.string.login_logo_description),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
            )
        }
    }
}

@Composable
private fun BrandWordmark(modifier: Modifier = Modifier) {
    val brand = ReelsBowlTheme.brand
    val wordmark = buildAnnotatedString {
        append(stringResource(R.string.login_brand_prefix))
        withStyle(SpanStyle(brush = brand.brandGradient)) {
            append(stringResource(R.string.login_brand_suffix))
        }
    }
    Text(
        text = wordmark,
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

@Composable
private fun BrandPillars(modifier: Modifier = Modifier) {
    val brand = ReelsBowlTheme.brand
    val pillars = listOf(
        R.string.login_pillar_stream,
        R.string.login_pillar_discover,
        R.string.login_pillar_connect,
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        pillars.forEachIndexed { index, pillarRes ->
            if (index > 0) {
                Box(
                    Modifier
                        .size(6.dp)
                        .background(brand.separator, CircleShape)
                )
            }
            Text(
                text = stringResource(pillarRes),
                style = MaterialTheme.typography.titleMedium,
                color = brand.accentPink,
            )
        }
    }
}

@Composable
private fun FeatureBadges(modifier: Modifier = Modifier) {
    val brand = ReelsBowlTheme.brand
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FeatureBadge(R.string.login_perk_quality, brand.accentCyan)
            FeatureBadge(R.string.login_perk_no_ads, brand.accentPink)
        }
        FeatureBadge(R.string.login_perk_creator_hub, brand.accentViolet)
    }
}

@Composable
private fun FeatureBadge(
    @StringRes labelRes: Int,
    sparkleColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(ReelsBowlTheme.brand.surfaceBadge)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "✦", style = MaterialTheme.typography.labelMedium, color = sparkleColor)
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

// endregion

// region Authentication

@Composable
private fun AuthenticationSection(
    isSigningIn: Boolean,
    onContinueWithGoogle: () -> Unit,
    onContinueAsGuest: () -> Unit,
    onOpenTerms: () -> Unit,
    onOpenPrivacy: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val brand = ReelsBowlTheme.brand
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        GoogleAuthButton(isSigningIn = isSigningIn, onClick = onContinueWithGoogle)
        GuestButton(onClick = onContinueAsGuest)

        Text(
            text = stringResource(R.string.login_sync_note),
            style = MaterialTheme.typography.bodySmall,
            color = brand.textSecondary.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier,
        )

        LegalNotice(onOpenTerms = onOpenTerms, onOpenPrivacy = onOpenPrivacy)
        EditorialFooter()
    }
}

@Composable
private fun GoogleAuthButton(
    isSigningIn: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable(enabled = !isSigningIn, onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isSigningIn) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.background,
                strokeWidth = 2.dp,
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_google_logo),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            text = stringResource(R.string.login_continue_with_google),
            style = MaterialTheme.typography.labelLarge,
            color = Color.Black,
        )
    }
}

@Composable
private fun GuestButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val brand = ReelsBowlTheme.brand
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(brand.surfacePill.copy(alpha = 0.8f))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_play_circle),
            contentDescription = null,
            modifier = Modifier.size(15.dp),
        )
        Text(
            text = stringResource(R.string.login_explore_as_guest),
            style = MaterialTheme.typography.bodyMedium,
            color = brand.textSecondary,
        )
    }
}

@Composable
private fun LegalNotice(
    onOpenTerms: () -> Unit,
    onOpenPrivacy: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val brand = ReelsBowlTheme.brand
    val linkStyles = TextLinkStyles(
        style = SpanStyle(color = brand.textSecondary, textDecoration = TextDecoration.Underline)
    )
    val notice = buildAnnotatedString {
        append(stringResource(R.string.login_legal_prefix))
        withLink(LinkAnnotation.Clickable("terms", linkStyles) { onOpenTerms() }) {
            append(stringResource(R.string.login_legal_terms))
        }
        append(stringResource(R.string.login_legal_conjunction))
        withLink(LinkAnnotation.Clickable("privacy", linkStyles) { onOpenPrivacy() }) {
            append(stringResource(R.string.login_legal_privacy))
        }
        append(stringResource(R.string.login_legal_suffix))
    }
    Text(
        text = notice,
        style = LegalTextStyle,
        color = brand.textTertiary,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

@Composable
private fun EditorialFooter(modifier: Modifier = Modifier) {
    val brand = ReelsBowlTheme.brand
    Row(
        modifier = modifier.padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(6.dp)
                .background(brand.accentPink.copy(alpha = 0.4f), CircleShape)
        )
        Text(
            text = stringResource(R.string.login_footer),
            style = MaterialTheme.typography.labelSmall,
            color = brand.textSecondary.copy(alpha = 0.4f),
        )
    }
}

/**
 * The three blurred colour orbs behind the screen. Drawn as radial gradients because
 * `Modifier.blur` is a no-op below API 31 and would leave hard-edged circles on older devices.
 */
@Composable
@ReadOnlyComposable
private fun Modifier.ambientGlow(): Modifier {
    val brand = ReelsBowlTheme.brand
    return drawBehind {
        fun orb(color: Color, center: Offset, radius: Float) = drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color, Color.Transparent),
                center = center,
                radius = radius,
            ),
            radius = radius,
            center = center,
        )
        orb(brand.glowPink, Offset(64.dp.toPx(), 48.dp.toPx()), 200.dp.toPx())
        orb(
            brand.glowViolet,
            Offset(size.width - 64.dp.toPx(), size.height * 0.5f + 160.dp.toPx()),
            220.dp.toPx(),
        )
        orb(brand.glowMagenta, Offset(size.width / 2f, size.height - 80.dp.toPx()), 220.dp.toPx())
    }
}

/** The 112dp emblem plus room for its halo to fall off. */
private val EmblemHaloSize = 160.dp
