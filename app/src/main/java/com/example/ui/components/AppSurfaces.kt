package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState

enum class MaterialVariant {
    Regular,
    Prominent,
    Subtle,
    Elevated
}

typealias LiquidGlassVariant = MaterialVariant

val LocalHazeState = staticCompositionLocalOf<HazeState?> { null }

fun Modifier.liquidGlassRim(
    shape: Shape = RoundedCornerShape(16.dp),
    isDark: Boolean = true,
    borderWidth: Dp = 1.dp,
    strokeWidth: Dp = borderWidth,
    variant: MaterialVariant = MaterialVariant.Regular,
    tint: Color? = null
): Modifier = this.border(
    width = strokeWidth,
    color = if (isDark) Color(0xFF292929) else Color(0xFFE5E5EA),
    shape = shape
)

@Composable
fun AppSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    variant: MaterialVariant = MaterialVariant.Regular,
    content: @Composable BoxScope.() -> Unit
) {
    val bgColor = when (variant) {
        MaterialVariant.Elevated -> Color(0xFF1A1A1A)
        MaterialVariant.Prominent -> Color(0xFF1A1A1A)
        MaterialVariant.Subtle -> Color(0xFF0D0D0D)
        MaterialVariant.Regular -> Color(0xFF111111)
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(bgColor, shape)
            .border(1.dp, Color(0xFF292929), shape),
        content = content
    )
}

@Composable
fun LiquidGlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    variant: MaterialVariant = MaterialVariant.Regular,
    content: @Composable BoxScope.() -> Unit
) {
    AppSurface(modifier = modifier, shape = shape, variant = variant, content = content)
}

@Composable
fun LiquidGlassBackdrop(
    shape: Shape,
    variant: MaterialVariant = MaterialVariant.Regular,
    isDark: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(if (isDark) Color(0xFF111111) else Color.White, shape)
    )
}

@Composable
fun LiquidGlassHighlight(
    shape: Shape,
    variant: MaterialVariant = MaterialVariant.Regular,
    isDark: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(shape)
            .border(1.dp, if (isDark) Color(0xFF292929) else Color(0xFFE5E5EA), shape)
    )
}
