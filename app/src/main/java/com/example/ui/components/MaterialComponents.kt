package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.AppDestination
import com.example.ui.navigation.AppFloatingAddButton
import com.example.ui.navigation.AppNavigationBar
import com.example.ui.theme.LocalAppColors
import com.example.ui.theme.StrongFontFamily
import com.example.util.HapticManager
import dev.chrisbanes.haze.HazeState
import kotlin.math.roundToInt

@Composable
fun MaterialSurface(
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(16.dp),
  variant: MaterialVariant = MaterialVariant.Regular,
  tint: Color? = null,
  borderWidth: Dp = 1.dp,
  backgroundColor: Color? = null,
  borderColor: Color? = null,
  elevation: Dp = 0.dp,
  hazeState: HazeState? = null,
  onClick: (() -> Unit)? = null,
  content: @Composable BoxScope.() -> Unit
) {
  val isDark = LocalAppColors.current.isDark
  val clickModifier = if (onClick != null) {
    Modifier.clickable(onClick = onClick)
  } else Modifier

  val resolvedBg = backgroundColor ?: when (variant) {
    MaterialVariant.Elevated, MaterialVariant.Prominent -> if (isDark) Color(0xFF1A1A1A) else Color(0xFFF2F2F2)
    MaterialVariant.Subtle -> if (isDark) Color(0xFF0D0D0D) else Color(0xFFFAFAFA)
    MaterialVariant.Regular -> if (isDark) Color(0xFF111111) else Color(0xFFFFFFFF)
  }

  val resolvedBorder = borderColor ?: if (isDark) Color(0xFF292929) else Color(0xFFE5E5EA)

  Box(
    modifier = modifier
      .clip(shape)
      .background(resolvedBg, shape)
      .border(borderWidth, resolvedBorder, shape)
      .then(clickModifier),
    content = content
  )
}

@Composable
fun LiquidGlassSurface(
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(16.dp),
  variant: MaterialVariant = MaterialVariant.Regular,
  tint: Color? = null,
  borderWidth: Dp = 1.dp,
  backgroundColor: Color? = null,
  borderColor: Color? = null,
  elevation: Dp = 0.dp,
  hazeState: HazeState? = null,
  onClick: (() -> Unit)? = null,
  content: @Composable BoxScope.() -> Unit
) {
  MaterialSurface(
    modifier = modifier,
    shape = shape,
    variant = variant,
    tint = tint,
    borderWidth = borderWidth,
    backgroundColor = backgroundColor,
    borderColor = borderColor,
    elevation = elevation,
    hazeState = hazeState,
    onClick = onClick,
    content = content
  )
}

@Composable
fun MaterialCard(
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(16.dp),
  cornerRadius: Dp? = null,
  variant: MaterialVariant = MaterialVariant.Regular,
  tint: Color? = null,
  backgroundColor: Color? = null,
  borderColor: Color? = null,
  borderWidth: Dp = 1.dp,
  elevation: Dp = 0.dp,
  hazeState: HazeState? = null,
  onClick: (() -> Unit)? = null,
  content: @Composable BoxScope.() -> Unit
) {
  val resolvedShape = if (cornerRadius != null) RoundedCornerShape(cornerRadius) else shape
  MaterialSurface(
    modifier = modifier,
    shape = resolvedShape,
    variant = variant,
    tint = tint,
    borderWidth = borderWidth,
    backgroundColor = backgroundColor,
    borderColor = borderColor,
    elevation = elevation,
    hazeState = hazeState,
    onClick = onClick,
    content = content
  )
}

@Composable
fun LiquidGlassCard(
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(16.dp),
  cornerRadius: Dp? = null,
  variant: MaterialVariant = MaterialVariant.Regular,
  tint: Color? = null,
  backgroundColor: Color? = null,
  borderColor: Color? = null,
  borderWidth: Dp = 1.dp,
  elevation: Dp = 0.dp,
  hazeState: HazeState? = null,
  onClick: (() -> Unit)? = null,
  content: @Composable BoxScope.() -> Unit
) {
  MaterialCard(
    modifier = modifier,
    shape = shape,
    cornerRadius = cornerRadius,
    variant = variant,
    tint = tint,
    backgroundColor = backgroundColor,
    borderColor = borderColor,
    borderWidth = borderWidth,
    elevation = elevation,
    hazeState = hazeState,
    onClick = onClick,
    content = content
  )
}

@Composable
fun LiquidGlassCapsule(
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(20.dp),
  variant: MaterialVariant = MaterialVariant.Regular,
  tint: Color? = null,
  borderWidth: Dp = 1.dp,
  hazeState: HazeState? = null,
  content: @Composable BoxScope.() -> Unit
) {
  MaterialSurface(
    modifier = modifier,
    shape = shape,
    variant = variant,
    tint = tint,
    borderWidth = borderWidth,
    hazeState = hazeState,
    content = content
  )
}

@Composable
fun MaterialSegmentedControl(
  items: List<String>,
  selectedIndex: Int,
  onItemSelected: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val isDark = LocalAppColors.current.isDark
  val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
  if (items.isEmpty()) return

  val controlShape = RoundedCornerShape(19.dp)
  val selectItem by rememberUpdatedState(onItemSelected)
  var dragTargetPos by remember { mutableFloatStateOf(selectedIndex.toFloat()) }

  BoxWithConstraints(
    modifier = modifier
      .fillMaxWidth()
      .height(48.dp)
      .clip(controlShape)
      .background(if (isDark) Color(0xFF1A1A1A) else Color(0xFFE5E5EA))
      .border(1.dp, if (isDark) Color(0xFF292929) else Color(0xFFD1D1D6), controlShape)
      .padding(3.dp)
  ) {
    val totalSlots = items.size
    val slotWidthPx = constraints.maxWidth.toFloat() / totalSlots

    Row(
      modifier = Modifier
        .fillMaxSize()
        .selectableGroup()
        .pointerInput(totalSlots, slotWidthPx, isRtl) {
          if (slotWidthPx <= 0f) return@pointerInput
          detectHorizontalDragGestures(
            onDragStart = { offset ->
              dragTargetPos = tabPositionAt(offset.x, slotWidthPx, totalSlots, isRtl)
            },
            onDragCancel = {},
            onDragEnd = {
              val committed = dragTargetPos.roundToInt().coerceIn(0, totalSlots - 1)
              selectItem(committed)
              HapticManager.buttonTap(context)
            },
            onHorizontalDrag = { change, _ ->
              change.consume()
              dragTargetPos = tabPositionAt(change.position.x, slotWidthPx, totalSlots, isRtl)
            }
          )
        }
    ) {
      items.forEachIndexed { index, label ->
        val isSelected = index == selectedIndex
        val interactions = remember { MutableInteractionSource() }
        Box(
          modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) (if (isDark) Color(0xFF4E4E4E) else Color.White) else Color.Transparent)
            .selectable(
              selected = isSelected,
              role = Role.Tab,
              interactionSource = interactions,
              indication = null,
              onClick = {
                selectItem(index)
                HapticManager.buttonTap(context)
              }
            ),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = label,
            style = TextStyle(
              fontSize = 13.sp,
              fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
              color = if (isSelected) (if (isDark) Color(0xFFF2F2F2) else Color(0xFF111111)) else Color(0xFF929292)
            ),
            maxLines = 1
          )
        }
      }
    }
  }
}

@Composable
fun LiquidGlassSegmentedControl(
  items: List<String>,
  selectedIndex: Int,
  onItemSelected: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  MaterialSegmentedControl(
    items = items,
    selectedIndex = selectedIndex,
    onItemSelected = onItemSelected,
    modifier = modifier
  )
}

@Composable
fun GlassSegmented(
  options: List<String>,
  value: Int,
  onChange: (Int) -> Unit,
  modifier: Modifier = Modifier,
  contentDescription: String? = "Segmented control"
) {
  MaterialSegmentedControl(
    items = options,
    selectedIndex = value,
    onItemSelected = onChange,
    modifier = modifier.semantics {
      if (contentDescription != null) {
        this.contentDescription = contentDescription
      }
    }
  )
}

@Composable
fun MaterialButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(12.dp),
  variant: MaterialVariant = MaterialVariant.Regular,
  tint: Color? = null,
  enabled: Boolean = true,
  icon: (@Composable () -> Unit)? = null,
  content: @Composable () -> Unit
) {
  val isDark = LocalAppColors.current.isDark
  val bgColor = when (variant) {
    MaterialVariant.Prominent -> if (isDark) Color(0xFFF2F2F2) else Color(0xFF111111)
    MaterialVariant.Elevated -> if (isDark) Color(0xFF222222) else Color(0xFFF2F2F2)
    MaterialVariant.Subtle -> if (isDark) Color(0xFF141414) else Color(0xFFF7F7F7)
    MaterialVariant.Regular -> if (isDark) Color(0xFF1A1A1A) else Color(0xFFEFEFEF)
  }

  Box(
    modifier = modifier
      .defaultMinSize(minHeight = 36.dp)
      .clip(shape)
      .background(bgColor, shape)
      .border(1.dp, if (isDark) Color(0xFF292929) else Color(0xFFE5E5EA), shape)
      .clickable(enabled = enabled, onClick = onClick)
      .padding(horizontal = 14.dp, vertical = 8.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      if (icon != null) {
        icon()
      }
      content()
    }
  }
}

@Composable
fun LiquidGlassButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(12.dp),
  variant: MaterialVariant = MaterialVariant.Regular,
  tint: Color? = null,
  enabled: Boolean = true,
  icon: (@Composable () -> Unit)? = null,
  content: @Composable () -> Unit
) {
  MaterialButton(
    onClick = onClick,
    modifier = modifier,
    shape = shape,
    variant = variant,
    tint = tint,
    enabled = enabled,
    icon = icon,
    content = content
  )
}

@Composable
fun MaterialIconButton(
  onClick: () -> Unit,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  size: Dp = 48.dp,
  variant: MaterialVariant = MaterialVariant.Regular,
  tint: Color? = null,
  enabled: Boolean = true,
  shape: Shape = CircleShape,
  icon: @Composable () -> Unit
) {
  val isDark = LocalAppColors.current.isDark
  val visualSize = size.coerceIn(32.dp, 48.dp)

  Box(
    modifier = modifier
      .size(48.dp)
      .clickable(enabled = enabled, onClick = onClick),
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .size(visualSize)
        .clip(shape)
        .background(if (isDark) Color(0xFF1A1A1A) else Color(0xFFF2F2F2), shape)
        .border(1.dp, if (isDark) Color(0xFF292929) else Color(0xFFE5E5EA), shape)
        .semantics {
          if (contentDescription != null) this.contentDescription = contentDescription
          role = Role.Button
        },
      contentAlignment = Alignment.Center
    ) {
      icon()
    }
  }
}

@Composable
fun LiquidGlassIconButton(
  onClick: () -> Unit,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  size: Dp = 48.dp,
  variant: MaterialVariant = MaterialVariant.Regular,
  tint: Color? = null,
  enabled: Boolean = true,
  shape: Shape = CircleShape,
  icon: @Composable () -> Unit
) {
  MaterialIconButton(
    onClick = onClick,
    contentDescription = contentDescription,
    modifier = modifier,
    size = size,
    variant = variant,
    tint = tint,
    enabled = enabled,
    shape = shape,
    icon = icon
  )
}

@Composable
fun LiquidGlassNavigationDock(
  destinations: List<AppDestination>,
  selectedTab: Int,
  onDestinationSelected: (AppDestination) -> Unit,
  modifier: Modifier = Modifier,
  hazeState: HazeState? = null,
  externalPageOffsetProvider: (() -> Float)? = null,
  externalPageOffset: Float? = null,
  onDragOffset: ((fraction: Float) -> Unit)? = null,
  onDragReleased: ((targetIndex: Int) -> Unit)? = null
) {
  AppNavigationBar(
    destinations = destinations,
    selectedTab = selectedTab,
    onDestinationSelected = onDestinationSelected,
    modifier = modifier
  )
}

@Composable
fun LiquidGlassNavItem(
  destination: AppDestination,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  onPressedChange: (Boolean) -> Unit = {}
) {
  val isDark = LocalAppColors.current.isDark
  val interactions = remember { MutableInteractionSource() }
  val pressed by interactions.collectIsPressedAsState()
  val notifyPressed by rememberUpdatedState(onPressedChange)
  LaunchedEffect(pressed) { notifyPressed(pressed) }

  Box(
    modifier = modifier
      .fillMaxHeight()
      .testTag("nav_${destination.name.lowercase()}")
      .semantics { contentDescription = destination.label }
      .selectable(
        selected = isSelected,
        role = Role.Tab,
        interactionSource = interactions,
        indication = null,
        onClick = onClick
      ),
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .height(36.dp)
        .width(52.dp)
        .clip(RoundedCornerShape(18.dp))
        .background(if (isSelected) (if (isDark) Color(0xFF4E4E4E) else Color(0xFFE5E5EA)) else Color.Transparent),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = if (isSelected) destination.iconFilled else destination.iconOutlined,
        contentDescription = null,
        tint = if (isSelected) (if (isDark) Color(0xFFF2F2F2) else Color(0xFF111111)) else Color(0xFF929292),
        modifier = Modifier.size(22.dp)
      )
    }
  }
}

@Composable
fun FloatingLiquidGlassActionButton(
  isOpen: Boolean = false,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  AppFloatingAddButton(
    onClick = onClick,
    modifier = modifier
  )
}

@Composable
fun LiquidGlassFloatingActionButton(
  onClick: () -> Unit,
  isOpen: Boolean = false,
  modifier: Modifier = Modifier
) {
  AppFloatingAddButton(
    onClick = onClick,
    modifier = modifier
  )
}

@Composable
fun LiquidGlassProgressIndicator(
  progress: Float,
  modifier: Modifier = Modifier,
  strokeWidth: Dp = 6.dp,
  trackColor: Color? = null,
  progressColor: Color? = null
) {
  val isDark = LocalAppColors.current.isDark
  val resolvedTrack = trackColor ?: if (isDark) Color(0xFF1A1A1A) else Color(0xFFE5E5EA)
  val resolvedProgress = progressColor ?: if (isDark) Color(0xFFF2F2F2) else Color(0xFF111111)
  val animatedProgress by animateFloatAsState(
    targetValue = progress.coerceIn(0f, 1f),
    animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
    label = "matProgressRing"
  )

  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
      val strokePx = strokeWidth.toPx()
      val arcSize = size.minDimension - strokePx
      val topLeft = Offset((size.width - arcSize) / 2f, (size.height - arcSize) / 2f)

      drawArc(
        color = resolvedTrack,
        startAngle = -90f,
        sweepAngle = 360f,
        useCenter = false,
        topLeft = topLeft,
        size = androidx.compose.ui.geometry.Size(arcSize, arcSize),
        style = Stroke(width = strokePx, cap = StrokeCap.Round)
      )

      if (animatedProgress > 0f) {
        drawArc(
          color = resolvedProgress,
          startAngle = -90f,
          sweepAngle = animatedProgress * 360f,
          useCenter = false,
          topLeft = topLeft,
          size = androidx.compose.ui.geometry.Size(arcSize, arcSize),
          style = Stroke(width = strokePx, cap = StrokeCap.Round)
        )
      }
    }

    Text(
      text = "${(animatedProgress * 100).toInt()}%",
      style = TextStyle(
        fontFamily = StrongFontFamily,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = if (isDark) Color(0xFFF2F2F2) else Color(0xFF111111)
      )
    )
  }
}
