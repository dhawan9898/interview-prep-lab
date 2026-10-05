package com.interviewpreplab.ui.theme

import androidx.compose.ui.graphics.Color

// CoreStack Dark IDE-Inspired Theme (Systems & Kernel Engineering Aesthetic)
// Dark theme (CoreStack forces dark mode for IDE-like experience)
val PrimaryDark = Color(0xFF00D9FF)        // Cyan accent for primary actions
val OnPrimaryDark = Color(0xFF080F16)      // Deep black for text on cyan
val PrimaryContainerDark = Color(0xFF004D61) // Darker cyan for containers
val OnPrimaryContainerDark = Color(0xFF00D9FF)

val SecondaryDark = Color(0xFFA0D995)      // Green accent for success/done
val OnSecondaryDark = Color(0xFF080F16)
val SecondaryContainerDark = Color(0xFF2D5E3F) // Darker green for containers
val OnSecondaryContainerDark = Color(0xFFA0D995)

val TertiaryDark = Color(0xFFFF6B9D)       // Pink/magenta for warnings/attention
val OnTertiaryDark = Color(0xFF080F16)
val TertiaryContainerDark = Color(0xFF663355) // Darker pink for containers
val OnTertiaryContainerDark = Color(0xFFFF6B9D)

val ErrorDark = Color(0xFFFF6B6B)          // Red for errors
val OnErrorDark = Color(0xFF080F16)
val ErrorContainerDark = Color(0xFF661A1A)
val OnErrorContainerDark = Color(0xFFFF6B6B)

val BackgroundDark = Color(0xFF080F16)     // Deep black background (IDE aesthetic)
val OnBackgroundDark = Color(0xFFCFD5E0)   // Light gray text

val SurfaceDark = Color(0xFF0F1A23)        // Slightly lighter surface
val OnSurfaceDark = Color(0xFFCFD5E0)
val SurfaceVariantDark = Color(0xFF1A2534) // For raised surfaces
val OnSurfaceVariantDark = Color(0xFFA8AEB8)

val OutlineDark = Color(0xFF36425F)        // Muted outline

// Light theme (fallback, but CoreStack is primarily dark)
val PrimaryLight = Color(0xFF006688)
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFCAE7FF)
val OnPrimaryContainerLight = Color(0xFF001F2E)

val SecondaryLight = Color(0xFF4A5F73)
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFFCCE4FA)
val OnSecondaryContainerLight = Color(0xFF031C33)

val TertiaryLight = Color(0xFF625B6F)
val OnTertiaryLight = Color(0xFFFFFFFF)
val TertiaryContainerLight = Color(0xFFEBDEF7)
val OnTertiaryContainerLight = Color(0xFF1E1629)

val ErrorLight = Color(0xFFB3261E)
val OnErrorLight = Color(0xFFFFFFFF)
val ErrorContainerLight = Color(0xFFF9DEDC)
val OnErrorContainerLight = Color(0xFF410E0B)

val BackgroundLight = Color(0xFFFBFCFE)
val OnBackgroundLight = Color(0xFF191C1E)

val SurfaceLight = Color(0xFFFBFCFE)
val OnSurfaceLight = Color(0xFF191C1E)
val SurfaceVariantLight = Color(0xFFDEE3EB)
val OnSurfaceVariantLight = Color(0xFF42474E)

val OutlineLight = Color(0xFF73787F)

// CoreStack accent colors (systems programming visualization)
val MemoryAccent = Color(0xFF00D9FF)       // Cyan for memory addresses
val RegisterAccent = Color(0xFFFF6B9D)     // Pink for register values
val StackAccent = Color(0xFFA0D995)        // Green for stack
val HeapAccent = Color(0xFFFFB84D)         // Orange for heap
val KernelAccent = Color(0xFFFF8C42)       // Orange-red for kernel mode
val PacketAccent = Color(0xFF6BCB77)       // Light green for packets
