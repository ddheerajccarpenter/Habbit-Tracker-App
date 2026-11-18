package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.InkSecondary
import com.example.ui.theme.LocalAppColors
import com.example.ui.theme.SeramondeFontFamily

@Composable
fun MaterialTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
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
    val isDark = LocalAppColors.current.isDark

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(if (isDark) Color(0xFF000000) else Color(0xFFFFFFFF), shape)
            .border(1.dp, if (isDark) Color(0xFF292929) else Color(0xFFE5E5EA), shape)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.widthIn(min = 44.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (leftContent != null) {
                    leftContent()
                } else if (onBack != null) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1A1A1A))
                            .border(1.dp, Color(0xFF292929), CircleShape)
                            .clickable { onBack() }
                            .testTag("header_back_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = backLabel,
                            tint = Color(0xFFF2F2F2),
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }

            Text(
                text = title,
                style = TextStyle(
                    fontFamily = SeramondeFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color(0xFFF2F2F2) else Color(0xFF111111),
                    textAlign = TextAlign.Center
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            Box(
                modifier = Modifier.widthIn(min = 44.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (rightContent != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        rightContent()
                    }
                } else if (actionText != null && onActionClick != null) {
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFF1A1A1A))
                            .border(1.dp, Color(0xFF292929), RoundedCornerShape(18.dp))
                            .clickable { onActionClick() }
                            .padding(horizontal = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = actionText,
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFF2F2F2)
                            )
                        )
                    }
                } else if (actionIcon != null && onActionClick != null) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1A1A1A))
                            .border(1.dp, Color(0xFF292929), CircleShape)
                            .clickable { onActionClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = actionContentDescription,
                            tint = Color(0xFFF2F2F2),
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LiquidGlassTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
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
    MaterialTopAppBar(
        title = title,
        modifier = modifier,
        shape = shape,
        onBack = onBack,
        backLabel = backLabel,
        actionIcon = actionIcon,
        actionText = actionText,
        actionContentDescription = actionContentDescription,
        onActionClick = onActionClick,
        actionTestTag = actionTestTag,
        leftContent = leftContent,
        rightContent = rightContent
    )
}

@Composable
fun MaterialLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 4.dp,
    shape: Shape = RoundedCornerShape(2.dp),
    trackColor: Color? = null,
    progressColor: Color? = null
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "matProgress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(trackColor ?: Color(0xFF1A1A1A))
    ) {
        if (animatedProgress > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(shape)
                    .background(progressColor ?: Color(0xFFF2F2F2))
            )
        }
    }
}

@Composable
fun LiquidGlassLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 4.dp,
    shape: Shape = RoundedCornerShape(2.dp),
    trackColor: Color? = null,
    progressColor: Color? = null
) {
    MaterialLinearProgress(
        progress = progress,
        modifier = modifier,
        height = height,
        shape = shape,
        trackColor = trackColor,
        progressColor = progressColor
    )
}

@Composable
fun MaterialEffectContainer(
    spacing: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
        content()
    }
}

@Composable
fun LiquidGlassEffectContainer(
    spacing: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    MaterialEffectContainer(spacing = spacing, content = content)
}
