package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// =========================================================================
// Shared theme roles: warm cream light mode and neutral charcoal dark mode.
// Content surfaces are opaque for reliable contrast; optical controls retain translucency.
// =========================================================================

// Canonical Dark Tokens (designglass.md §02B — Neutral Charcoal Environment)
val CanonicalCanvasDark = Color(0xFF121212)
val CanonicalCanvasSecondaryDark = Color(0xFF1C1C1C)
val CanonicalGlassFillBaseDark = Color(0xFF202020)
val CanonicalGlassFillElevatedDark = Color(0xFF2A2A2A)
val CanonicalGlassFillSubtleDark = Color(0xFF191919)
val CanonicalGlassBorderTopDark = Color(0x40FFFFFF) // Solid Specular Hairline
val CanonicalGlassBorderBottomDark = Color(0x18FFFFFF)
val CanonicalGlassSheenTopDark = Color(0x40FFFFFF)
val CanonicalTextPrimaryDark = Color(0xFFF2F2F2)
val CanonicalTextSecondaryDark = Color(0xFFCECECE)
val CanonicalTextMutedDark = Color(0xFFA8A8A8)
val CanonicalDividerDark = Color(0x26FFFFFF) // Solid interior separator
val CanonicalAccentActionDark = Color(0xFFF2F2F2)
val CanonicalDestructiveDark = Color(0xFFF87171)

// =========================================================================
// Authentic Liquid Glass Design System Tokens (Zero Blur)
// Smoky transparent graphite, neutral grey translucency, crisp silver highlights
// =========================================================================
val LiquidGlassCanvasDark = CanonicalCanvasDark
val LiquidGlassCanvasSecondaryDark = CanonicalCanvasSecondaryDark

// Translucent Layered Materials
val LiquidGlassCardTopDark = Color(0x382E2E2E)
val LiquidGlassCardBottomDark = Color(0x22202020)
val LiquidGlassCapsuleTopDark = Color(0x40383838)
val LiquidGlassCapsuleBottomDark = Color(0x28262626)
val LiquidGlassLensTopDark = Color(0x4DFFFFFF)
val LiquidGlassLensMidDark = Color(0x26FFFFFF)
val LiquidGlassLensBottomDark = Color(0x12262626)

// Specular Rims & Hairline Highlights (Zero Blur)
val LiquidGlassBorderSpecularTop = Color(0x59FFFFFF)   // 35% crisp overhead specular highlight
val LiquidGlassBorderSpecularMid = Color(0x24FFFFFF)   // 14% lateral decay
val LiquidGlassBorderSpecularBottom = Color(0x0C000000)// 5% subtle dark rim
val LiquidGlassBorderSubtle = Color(0x20FFFFFF)        // Interior capsule separators

val LiquidGlassButtonResting = Color(0x33303030)    // 20% alpha
val LiquidGlassButtonPressed = Color(0x52424242)    // 32% alpha

// Canonical Light Tokens (Warm Ivory & Solid Surface System)
val CanonicalCanvasLight = Color.White
val CanonicalCanvasSecondaryLight = Color(0xFFF7F7F7)
val CanonicalGlassFillBaseLight = Color(0xD9FFFFFF)
val CanonicalGlassFillElevatedLight = Color(0xF2FFFFFF)
val CanonicalGlassFillSubtleLight = Color(0xBFFFFFFF)
val CanonicalGlassBorderTopLight = Color(0xFFCBCBCB)
val CanonicalGlassBorderBottomLight = Color(0xFFCBCBCB)
val CanonicalGlassSheenTopLight = Color(0xFFCBCBCB)
val CanonicalTextPrimaryLight = Color(0xFF4A4A4A)
val CanonicalTextSecondaryLight = Color(0xFF666666)
val CanonicalTextMutedDarkLight = Color(0xFF7A7D82)
val CanonicalTextMutedLight = Color(0xFF7A7D82)
val CanonicalDividerLight = Color(0xFFCBCBCB)
val CanonicalAccentActionLight = Color(0xFF4A4A4A)
val CanonicalDestructiveLight = Color(0xFF555555)

val BrandCharcoalDark = CanonicalCanvasDark
val BrandCharcoalElevated = CanonicalCanvasSecondaryDark
val BrandGlassSurface = CanonicalGlassFillBaseDark
val BrandTextPrimary = CanonicalTextPrimaryDark
val BrandTextSecondary = CanonicalTextSecondaryDark
val BrandTextMuted = CanonicalTextMutedDark
val BrandGlassBorder = CanonicalGlassBorderTopDark
val BrandGlassHighlight = CanonicalGlassSheenTopDark

// Aliases for system compatibility
val BrandDarkGrey = BrandCharcoalDark
val BrandWhite = BrandTextPrimary

val BrandCyprus = BrandCharcoalDark
val BrandSand = BrandTextPrimary

// Backward compatible aliases
val PaletteLilacAsh = BrandTextPrimary
val PaletteAntiqueWhite = BrandTextPrimary
val PaletteVintageGrape = BrandCharcoalDark
val PaletteBerryCrush = BrandCharcoalDark
val PaletteMintLeaf = BrandTextPrimary

// =========================================================================
// Ink Wash Universal Palette (Authentic Japanese Ink & Neutral Parchment)
// Canonical Swatches (Premium Monochromatic & Refined Warm Off-White):
// 1. Charcoal Slate:      #171717 / #4A4A4A
// 2. Mist Silver:         #CBCBCB / #D6D6DC
// 3. Off-White Canvas:    #FAFAF7 (Purged all yellow/cream #FFFFE3)
// 4. Slate Steel Wash:    #5E6E82 / #6D8196
// Zero yellow cast, zero neon blue, zero neon green, zero AI-sloped colors.
// =========================================================================
val InkWashCharcoal = Color.Black
val InkWashMist = Color(0xFFB0B0B0)
val InkWashIvory = Color(0xFFFFFFFF)          // Crisp white primary text
val InkWashSlate = Color(0xFF555555)

// Tonal derivatives ensuring Dark Mode is darker than Light Mode
val InkWashCharcoalDeep = CanonicalCanvasDark
val InkWashCharcoalSurface = CanonicalGlassFillBaseDark
val InkWashCharcoalElevated = CanonicalGlassFillElevatedDark
val InkWashCharcoalBorder = Color(0x14FFFFFF) // Subtle Hairline Border (8% white)
val InkWashIvoryPaper = Color.White
val InkWashIvoryCard = Color(0xB8FFFFFF)      // Primary glass surface: rgba(255, 255, 255, 0.72)
val InkWashIvoryElevated = Color(0xD6FFFFFF)  // Elevated glass surface: rgba(255, 255, 255, 0.84)
val InkWashRustMuted = Color(0xFF555555)

// Semantic tokens are resolved from the active theme. Light mode uses slate ink;
// dark mode uses a lighter steel tint so small labels retain clear contrast.
val OklchIndigoSoft = Color(0x335E5E5E)
// These accessors are declared below LocalAppColors so every screen follows the
// same light/dark semantic palette rather than carrying its own accent colors.

// --- Dark Mode Tokens (Deep Charcoal & Translucent Glass) ---
val GrapeCanvasDark = InkWashCharcoalDeep
val GrapeSurfaceSoft = InkWashCharcoalSurface
val GrapeSurfaceCard = InkWashCharcoalSurface
val GrapeSurfaceElevated = InkWashCharcoalElevated
val GrapeHairline = InkWashCharcoalBorder
val GrapeHairlineStrong = InkWashMist

val PrimaryDarkCanvas = InkWashCharcoalDeep
val PrimaryAccentLime = Color(0xFFF0F0F2)

val BugattiCanvas = InkWashCharcoalDeep
val BugattiSurfaceSoft = GrapeSurfaceSoft
val BugattiSurfaceCard = GrapeSurfaceCard
val BugattiSurfaceElevated = GrapeSurfaceElevated
val BugattiHairline = GrapeHairline
val BugattiHairlineStrong = GrapeHairlineStrong

val BugattiInk = Color(0xFFF5F5F7)
val BugattiBody = InkWashMist
val BugattiBodyStrong = Color(0xFFF5F5F7)
val BugattiMuted = Color(0xFF8E9296)
val BugattiMutedSoft = Color(0xFF8E9296)

val BugattiLink = InkWashSlate
val BugattiWarning = InkWashSlate
val BugattiSuccess = InkWashSlate
val BugattiDanger = InkWashRustMuted

// --- Light Mode Tokens (Clean Off-White Canvas & Translucent Soft Surfaces) ---
val LightCanvas = InkWashIvoryPaper           // #FAFAF7
val LightCanvasSoft = Color(0xFFF5F5F0)       // #F5F5F0
val LightSurfaceCard = InkWashIvoryCard       // rgba(255, 255, 255, 0.72)
val LightSurfaceElevated = InkWashIvoryElevated// rgba(255, 255, 255, 0.84)
val LightHairline = Color(0x171E1E1E)         // rgba(30, 30, 30, 0.09)
val LightHairlineStrong = Color(0xFF4A4A4A)
val LightGlassSpecular = Color(0xCCFFFFFF)    // rgba(255, 255, 255, 0.80)

val LightInkPrimary = Color(0xFF171717)       // #171717
val LightInkSecondary = Color(0xFF66686C)     // #66686C
val LightInkMuted = Color(0xFF8E9096)

val MobbinCanvas = LightCanvas
val MobbinCanvasSoft = LightCanvasSoft
val MobbinField = Color(0xFFF2F2F5)
val MobbinHairline = LightHairline
val MobbinHairlineSoft = Color(0x121E1E1E)

val MobbinInk = LightInkPrimary
val MobbinInkSoft = LightInkSecondary
val MobbinTextMuted = LightInkMuted
val MobbinTextFaint = Color(0xFF9E9FA3)
val MobbinAccent = InkWashCharcoal

val MobbinSuccess = InkWashSlate
val MobbinWarning = InkWashSlate
val MobbinDanger = InkWashRustMuted

// Canonical TDlxHT Tri-Color System
val BlueHorizon = Color(0xFF2FA4D7)       // 1. Blue Horizon #2FA4D7: Primary Vibrant Accent
val DesertCream = Color(0xFFF5E9D8)       // 2. Desert Cream #F5E9D8: Warm Canvas & Default Background
val CocaNoir = Color(0xFF3E2C23)          // 3. Coca Noir #3E2C23: Nav Bar Liquid Glass, Shadow & Icon Tint
val LightTextCocoa = Color(0xFF574E42)    // Light mode text color #574e42
val LightTextMuted = Color(0xFF6E655B)

// Coca Noir Liquid Glass Tints (Translucent, never solid)
val CocaNoirNavGlassDark = Color(0x4D242424)   // Translucent neutral charcoal tint in dark mode
val CocaNoirNavGlassLight = Color(0x223E2C23)  // Translucent warm cocoa tint in light mode
val CocaNoirShadowDark = Color(0x99000000)     // Crisp ambient drop shadow in dark mode
val CocaNoirShadowLight = Color(0x383E2C23)    // Soft cocoa drop shadow

data class TriColorPalette(
  val id: String,
  val name: String,
  val solid: Color,
  val gradientStart: Color,
  val gradientEnd: Color
) {
  val gradientBrush: androidx.compose.ui.graphics.Brush
    get() = androidx.compose.ui.graphics.Brush.linearGradient(listOf(gradientStart, gradientEnd))
}

val CanonicalTriColorPalette = TriColorPalette(
  id = "TDLXHT_CANONICAL",
  name = "Horizon & Desert Noir",
  solid = BlueHorizon,
  gradientStart = BlueHorizon,
  gradientEnd = Color(0xFF72C7E8)
)

data class AppColors(
  val paperBackground: Color,
  val paperDotColor: Color,
  val inkPrimary: Color,
  val inkSecondary: Color,
  val inkMuted: Color,
  val cardBackground: Color,
  val cardBorder: Color,
  val pillBackgroundDark: Color,
  val pillTextDark: Color,
  val progressBarTrack: Color,
  val progressBarFilled: Color,
  val accentTerracotta: Color,
  val accentOrange: Color,
  val accentYellowHighlight: Color,
  val accentRed: Color,
  val chipBorder: Color,
  val chipUnselected: Color,
  val isDark: Boolean,
  val oklchPrimary: Color = BlueHorizon,
  val oklchSuccess: Color = BlueHorizon,
  val oklchWarning: Color = Color(0xFFF59E0B),
  val oklchDanger: Color = Color(0xFFEF4444),
  val oklchFocus: Color = BlueHorizon,
  val oklchPurple: Color = BlueHorizon,
  val surfaceCard: Color = if (isDark) Color(0x18FFFFFF) else Color(0xB8FFFFFF),
  val surfaceElevated: Color = if (isDark) Color(0x28FFFFFF) else Color(0xEAFFFFFF),
  val cocaNoirNavGlass: Color = if (isDark) CocaNoirNavGlassDark else CocaNoirNavGlassLight,
  val cocaNoirShadow: Color = if (isDark) CocaNoirShadowDark else CocaNoirShadowLight,
  val blueHorizonAccent: Color = BlueHorizon,
  val focusRing: Color = if (isDark) Color.White else CocaNoir,
  val focusHalo: Color = if (isDark) Color.Black else Color.White
)

val LightColors = AppColors(
  paperBackground = DesertCream,
  paperDotColor = Color.Transparent,
  inkPrimary = LightTextCocoa,
  inkSecondary = LightTextMuted,
  inkMuted = Color(0xFF70665C),
  cardBackground = Color(0x9EFFFFFF),
  cardBorder = Color(0x1F3E2C23),
  pillBackgroundDark = CocaNoir,
  pillTextDark = DesertCream,
  progressBarTrack = Color(0x1F3E2C23),
  progressBarFilled = BlueHorizon,
  accentTerracotta = CocaNoir,
  accentOrange = BlueHorizon,
  accentYellowHighlight = Color(0x182FA4D7),
  accentRed = Color(0xFFDC2626),
  chipBorder = Color(0x1A3E2C23),
  chipUnselected = Color(0x0C3E2C23),
  isDark = false,
  oklchPrimary = BlueHorizon,
  oklchSuccess = Color(0xFF34C759),
  oklchWarning = Color(0xFFF59E0B),
  oklchDanger = Color(0xFFDC2626),
  oklchFocus = BlueHorizon,
  oklchPurple = BlueHorizon,
  surfaceCard = Color(0x9EFFFFFF),
  surfaceElevated = Color(0xE0FFFFFF),
  cocaNoirNavGlass = CocaNoirNavGlassLight,
  cocaNoirShadow = CocaNoirShadowLight,
  blueHorizonAccent = BlueHorizon
)

val DarkColors = AppColors(
  paperBackground = CanonicalCanvasDark,
  paperDotColor = Color.Transparent,
  inkPrimary = CanonicalTextPrimaryDark,
  inkSecondary = CanonicalTextSecondaryDark,
  inkMuted = CanonicalTextMutedDark,
  cardBackground = CanonicalGlassFillBaseDark,
  cardBorder = Color(0x2EFFFFFF),
  pillBackgroundDark = Color(0xFFF2F2F2),
  pillTextDark = Color(0xFF121212),
  progressBarTrack = Color(0x24FFFFFF),
  progressBarFilled = Color(0xFF38BDF8),
  accentTerracotta = Color(0xFFE0E0E0),
  accentOrange = Color(0xFF38BDF8),
  accentYellowHighlight = Color(0x2638BDF8),
  accentRed = Color(0xFFF87171),
  chipBorder = Color(0x2EFFFFFF),
  chipUnselected = Color(0xFF191919),
  isDark = true,
  oklchPrimary = Color(0xFF38BDF8),
  oklchSuccess = Color(0xFF34D399),
  oklchWarning = Color(0xFFFBBF24),
  oklchDanger = Color(0xFFF87171),
  oklchFocus = Color(0xFF38BDF8),
  oklchPurple = Color(0xFF818CF8),
  surfaceCard = CanonicalGlassFillBaseDark,
  surfaceElevated = CanonicalGlassFillElevatedDark,
  cocaNoirNavGlass = CocaNoirNavGlassDark,
  cocaNoirShadow = CocaNoirShadowDark,
  blueHorizonAccent = Color(0xFF38BDF8),
  focusRing = Color(0xFF38BDF8),
  focusHalo = Color(0xFF121212)
)

val LocalAppColors = staticCompositionLocalOf { DarkColors }

val OklchIndigo: Color
  @Composable get() = LocalAppColors.current.oklchPrimary
val OklchEmerald: Color
  @Composable get() = LocalAppColors.current.oklchSuccess
val OklchEmeraldSoft: Color
  @Composable get() = LocalAppColors.current.oklchSuccess.copy(alpha = 0.16f)
val OklchAmber: Color
  @Composable get() = LocalAppColors.current.oklchWarning
val OklchAmberSoft: Color
  @Composable get() = LocalAppColors.current.oklchWarning.copy(alpha = 0.16f)
val OklchCoral: Color
  @Composable get() = LocalAppColors.current.oklchDanger
val OklchCoralSoft: Color
  @Composable get() = LocalAppColors.current.oklchDanger.copy(alpha = 0.16f)
val OklchCyan: Color
  @Composable get() = LocalAppColors.current.oklchFocus
val OklchCyanSoft: Color
  @Composable get() = LocalAppColors.current.oklchFocus.copy(alpha = 0.16f)
val OklchPurple: Color
  @Composable get() = LocalAppColors.current.oklchPurple
val OklchPurpleSoft: Color
  @Composable get() = LocalAppColors.current.oklchPurple.copy(alpha = 0.16f)

// Composable accessors allowing 100% backward compatibility across all screens
val PaperBackground: Color
  @Composable get() = LocalAppColors.current.paperBackground

val PaperDotColor: Color
  @Composable get() = LocalAppColors.current.paperDotColor

val InkPrimary: Color
  @Composable get() = LocalAppColors.current.inkPrimary

val InkSecondary: Color
  @Composable get() = LocalAppColors.current.inkSecondary

val InkMuted: Color
  @Composable get() = LocalAppColors.current.inkMuted

val CardBackground: Color
  @Composable get() = LocalAppColors.current.cardBackground

val CardBorder: Color
  @Composable get() = LocalAppColors.current.cardBorder

val PillBackgroundDark: Color
  @Composable get() = LocalAppColors.current.pillBackgroundDark

val PillTextDark: Color
  @Composable get() = LocalAppColors.current.pillTextDark

val ProgressBarTrack: Color
  @Composable get() = LocalAppColors.current.progressBarTrack

val ProgressBarFilled: Color
  @Composable get() = LocalAppColors.current.progressBarFilled

val AccentTerracotta: Color
  @Composable get() = LocalAppColors.current.accentTerracotta

val AccentOrange: Color
  @Composable get() = LocalAppColors.current.accentOrange

val AccentYellowHighlight: Color
  @Composable get() = LocalAppColors.current.accentYellowHighlight

val AccentRed: Color
  @Composable get() = LocalAppColors.current.accentRed

val ChipBorder: Color
  @Composable get() = LocalAppColors.current.chipBorder

val ChipUnselected: Color
  @Composable get() = LocalAppColors.current.chipUnselected

val AppleBlue: Color
  @Composable get() = LocalAppColors.current.oklchPrimary

val AppleGreen: Color
  @Composable get() = LocalAppColors.current.oklchSuccess

val AppleOrange: Color
  @Composable get() = LocalAppColors.current.oklchWarning

val PrimaryAccent: Color
  @Composable get() = LocalAppColors.current.oklchPrimary

val OklchPrimaryToken: Color
  @Composable get() = LocalAppColors.current.oklchPrimary

val OklchSuccessToken: Color
  @Composable get() = LocalAppColors.current.oklchSuccess

val OklchWarningToken: Color
  @Composable get() = LocalAppColors.current.oklchWarning

val OklchDangerToken: Color
  @Composable get() = LocalAppColors.current.oklchDanger

val OklchFocusToken: Color
  @Composable get() = LocalAppColors.current.oklchFocus

val OklchPurpleToken: Color
  @Composable get() = LocalAppColors.current.oklchPurple

val SurfaceCardToken: Color
  @Composable get() = LocalAppColors.current.surfaceCard

val SurfaceElevatedToken: Color
  @Composable get() = LocalAppColors.current.surfaceElevated
