package com.example.ui.glass

import com.skydoves.cloudy.cloudy

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import com.example.AppDestination
import dev.chrisbanes.haze.HazeState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardBorder
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.InkSecondary
import com.example.ui.theme.LocalAppColors
import com.example.ui.theme.SeramondeFontFamily
import com.example.ui.theme.StrongFontFamily

/**
 * Authentic iOS 26 Liquid Glass Top App Bar:
 * Pinned sticky header with live scene backdrop blur, AGSL edge refraction,
 * directional bottom specular rim, and seamless content scrolling underneath.
 */
@Composable
fun LiquidGlassTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(22.dp),
    onBack: (() -> Unit)? = null,
    backLabel: String = "Back",
    actionIcon: ImageVector? = null,
    actionText: String? = null,
    actionContentDescription: String? = null,
    onActionClick: (() -> Unit)? = null,
    actionTestTag: String? = null,
    leftContent: (@Composable () -> Unit)? = null,
    rightContent: (@Composable () -> Unit)? = null
) {
    val hazeState = LocalHazeState.current
    val isDark = LocalAppColors.current.isDark

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .then(
                if (hazeState != null) {
                    Modifier.liquidGlass(
                        variant = LiquidGlassVariant.Subtle,
                        shape = shape,
                        borderWidth = 0.75.dp
                    )
                } else {
                    Modifier.cloudy(radius = 24).background(
                        if (isDark) com.example.ui.theme.CanonicalGlassFillBaseDark.copy(alpha = 0.85f) else Color(0xEBFFFFFF)
                    )
                }
            )
            .then(
                if (shape == RectangleShape) {
                    Modifier.drawWithCache {
                        val strokePx = 0.75.dp.toPx()
                        val bottomRimBrush = Brush.horizontalGradient(
                            colors = if (isDark) {
                                listOf(
                                    Color.White.copy(alpha = 0.08f),
                                    Color.White.copy(alpha = 0.22f),
                                    Color.White.copy(alpha = 0.08f)
                                )
                            } else {
                                listOf(
                                    Color.White.copy(alpha = 0.20f),
                                    Color.White.copy(alpha = 0.55f),
                                    Color.White.copy(alpha = 0.20f)
                                )
                            }
                        )
                        onDrawWithContent {
                            drawContent()
                            drawLine(
                                brush = bottomRimBrush,
                                start = androidx.compose.ui.geometry.Offset(0f, size.height),
                                end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                                strokeWidth = strokePx
                            )
                        }
                    }
                } else Modifier
            )
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. Left Action (Back or custom control)
            Box(
                modifier = Modifier.widthIn(min = 44.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (leftContent != null) {
                    leftContent()
                } else if (onBack != null) {
                    com.example.ui.components.LiquidGlassIconButton(
                        onClick = onBack,
                        contentDescription = backLabel,
                        size = 38.dp,
                        variant = LiquidGlassVariant.Regular,
                        modifier = Modifier.testTag("header_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = InkPrimary,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }

            // 2. Center Title
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = SeramondeFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkPrimary,
                    textAlign = TextAlign.Center
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            // 3. Right Action
            Box(
                modifier = Modifier.widthIn(min = 44.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (rightContent != null) {
                    LiquidGlassEffectContainer(spacing = 8.dp) {
                        rightContent()
                    }
                } else if (actionText != null && onActionClick != null) {
                    com.example.ui.components.LiquidGlassButton(
                        onClick = onActionClick,
                        shape = RoundedCornerShape(12.dp),
                        variant = LiquidGlassVariant.Prominent,
                        modifier = Modifier
                            .height(34.dp)
                            .then(if (actionTestTag != null) Modifier.testTag(actionTestTag) else Modifier)
                    ) {
                        Text(
                            text = actionText,
                            style = TextStyle(
                                fontFamily = StrongFontFamily,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = LocalAppColors.current.oklchPrimary
                            )
                        )
                    }
                } else if (actionIcon != null && onActionClick != null) {
                    com.example.ui.components.LiquidGlassIconButton(
                        onClick = onActionClick,
                        contentDescription = actionContentDescription ?: "Action",
                        size = 38.dp,
                        variant = LiquidGlassVariant.Regular,
                        modifier = if (actionTestTag != null) Modifier.testTag(actionTestTag) else Modifier
                    ) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = null,
                            tint = InkPrimary,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Authentic iOS 26 Liquid Glass Linear Progress Bar:
 * Smoked translucent glass channel with hairline specular rim and glowing fluid glass indicator bead.
 */
@Composable
fun LiquidGlassLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 10.dp,
    trackColor: Color? = null,
    progressColor: Color? = null,
    shape: Shape = RoundedCornerShape(percent = 50)
) {
    val isDark = LocalAppColors.current.isDark
    val clamped = progress.coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = clamped,
        animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing),
        label = "liquidGlassLinearProgress"
    )

    val resolvedTrackColor = trackColor ?: if (isDark) {
        com.example.ui.theme.CanonicalGlassFillBaseDark.copy(alpha = 0.13f)
    } else {
        Color(0x22000000)
    }
    val resolvedProgressColor = progressColor ?: LocalAppColors.current.oklchPrimary

    Box(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .clip(shape)
            .background(resolvedTrackColor)
            .liquidGlassRim(shape, isDark, 0.75.dp, variant = LiquidGlassVariant.Subtle)
    ) {
        if (animatedProgress > 0f) {
            val fluidIndicatorBrush = Brush.horizontalGradient(
                listOf(
                    resolvedProgressColor.copy(alpha = 0.85f),
                    resolvedProgressColor
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(shape)
                    .background(fluidIndicatorBrush)
                    .drawWithCache {
                        // Overhead specular sheen on top half of the fluid indicator
                        val highlightBrush = Brush.verticalGradient(
                            0f to Color.White.copy(alpha = 0.45f),
                            0.5f to Color.White.copy(alpha = 0.10f),
                            1f to Color.Transparent
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(highlightBrush)
                        }
                    }
            )
        }
    }
}

/**
 * Authentic Apple iOS 26 Liquid Glass Floating Navigation Bar (glass.md Section 02.5 & 03):
 * 48dp floating glass dock in a 56dp container with navigation bar insets and 8dp bottom padding.
 * Features live Haze backdrop blur, AGSL RuntimeShader light bending with 10% chromatic dispersion,
 * razor-sharp 0.75dp specular hairline rim, 440ms traveling specular light sweep beam, 300ms tap bloom flare,
 * and elevated optical lens pill with dual-spring 120Hz drag kinetics.
 */
@Composable
fun GlassNav(
    destinations: List<AppDestination> = AppDestination.entries.take(4),
    selectedTab: Int,
    onDestinationSelected: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
    hazeState: HazeState? = null,
    showLabels: Boolean = false,
    externalPageOffsetProvider: (() -> Float)? = null,
    externalPageOffset: Float? = null,
    onDragOffset: ((Float) -> Unit)? = null,
    onDragReleased: ((Int) -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        com.example.ui.components.LiquidGlassNavigationDock(
            destinations = destinations.take(4),
            selectedTab = selectedTab.coerceIn(0, 3),
            externalPageOffsetProvider = externalPageOffsetProvider,
            externalPageOffset = externalPageOffset,
            onDragOffset = onDragOffset,
            onDragReleased = onDragReleased,
            onDestinationSelected = onDestinationSelected,
            hazeState = hazeState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

/**
 * Canonical alias for GlassNav matching the LiquidGlass naming system.
 */
@Composable
fun LiquidGlassNav(
    destinations: List<AppDestination> = AppDestination.entries.take(4),
    selectedTab: Int,
    onDestinationSelected: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
    hazeState: HazeState? = null,
    showLabels: Boolean = false,
    externalPageOffsetProvider: (() -> Float)? = null,
    externalPageOffset: Float? = null,
    onDragOffset: ((Float) -> Unit)? = null,
    onDragReleased: ((Int) -> Unit)? = null
) {
    GlassNav(
        destinations = destinations,
        selectedTab = selectedTab,
        onDestinationSelected = onDestinationSelected,
        modifier = modifier,
        hazeState = hazeState,
        showLabels = showLabels,
        externalPageOffsetProvider = externalPageOffsetProvider,
        externalPageOffset = externalPageOffset,
        onDragOffset = onDragOffset,
        onDragReleased = onDragReleased
    )
}

