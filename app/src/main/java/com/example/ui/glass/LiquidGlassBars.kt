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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
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
            .background(if (isDark) Color(0xFF000000) else Color(0xFFFFFFFF), shape)
            .border(0.5.dp, if (isDark) Color(0xFF292929) else Color(0xFFE0E0E0), shape)
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
        Color(0xFF1A1A1A)
    } else {
        Color(0xFFE0E0E0)
    }
    val resolvedProgressColor = progressColor ?: (if (isDark) Color(0xFFF2F2F2) else Color(0xFF111111))

    Box(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .clip(shape)
            .background(resolvedTrackColor)
    ) {
        if (animatedProgress > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(shape)
                    .background(resolvedProgressColor)
            )
        }
    }
}

/**
 * Minimal Monochromatic Floating Capsule Navigation Dock (M3 Strict Dark Mode):
 * 52dp height floating capsule with 4 icon-only destinations (Home, Calendar, Insights, Settings),
 * understated matte charcoal selection pill (#4E4E4E), soft white active icon (#F2F2F2),
 * and muted grey inactive icon (#929292).
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
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(Color(0xFF1A1A1A))
                .border(1.dp, Color(0xFF292929), RoundedCornerShape(26.dp))
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                destinations.take(4).forEachIndexed { index, destination ->
                    val isSelected = index == selectedTab
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .width(56.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) Color(0xFF4E4E4E) else Color.Transparent)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = Color.White)
                            ) {
                                com.example.util.HapticManager.lightTap(context)
                                onDestinationSelected(destination)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) destination.iconFilled else destination.iconOutlined,
                            contentDescription = destination.label,
                            tint = if (isSelected) Color(0xFFF2F2F2) else Color(0xFF929292),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Canonical alias for GlassNav matching the design token system.
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

