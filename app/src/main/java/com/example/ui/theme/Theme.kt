package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

enum class ThemeStyle {
    GREEN,
    ORANGE,
    BLUE,
    VIOLET
}

// 1. GREEN (Original Theme)
private val GreenDarkColorScheme =
  darkColorScheme(
    primary = Color(0xFF6EDCBA),
    secondary = Color(0xFFFFB68E),
    tertiary = Color(0xFF82D3E5),
    background = Color(0xFF0E1513),
    surface = Color(0xFF121B18),
    onPrimary = Color(0xFF00372A),
    onSecondary = Color(0xFF532200),
    onTertiary = Color(0xFF00363F),
    primaryContainer = Color(0xFF00513F),
    onPrimaryContainer = Color(0xFF8BF9D5),
    secondaryContainer = Color(0xFF753400),
    onSecondaryContainer = Color(0xFFFFDDBA),
    onBackground = Color(0xFFE0E3DE),
    onSurface = Color(0xFFE0E3DE),
    outline = Color(0xFF89938F),
    surfaceVariant = Color(0xFF3F4945),
    onSurfaceVariant = Color(0xFFBFC9C4)
  )

private val GreenLightColorScheme =
  lightColorScheme(
    primary = Color(0xFF006B54),
    secondary = Color(0xFF964900),
    tertiary = Color(0xFF006876),
    background = Color(0xFFF4FAF7),
    surface = Color(0xFFF9FFFC),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    primaryContainer = Color(0xFF8BF9D5),
    onPrimaryContainer = Color(0xFF002117),
    secondaryContainer = Color(0xFFFFDDBA),
    onSecondaryContainer = Color(0xFF311300),
    onBackground = Color(0xFF161D1A),
    onSurface = Color(0xFF161D1A),
    outline = Color(0xFF6F7975),
    surfaceVariant = Color(0xFFDBE5E0),
    onSurfaceVariant = Color(0xFF3F4945)
  )

// 2. ORANGE Theme
private val OrangeDarkColorScheme =
  darkColorScheme(
    primary = Color(0xFFFFB74D),
    secondary = Color(0xFFFF8A65),
    tertiary = Color(0xFFFFD54F),
    background = Color(0xFF19130D),
    surface = Color(0xFF1F1813),
    onPrimary = Color(0xFF4E2600),
    onSecondary = Color(0xFF5B1A00),
    onTertiary = Color(0xFF402D00),
    primaryContainer = Color(0xFF703800),
    onPrimaryContainer = Color(0xFFFFDDBE),
    secondaryContainer = Color(0xFF812600),
    onSecondaryContainer = Color(0xFFFFDBCF),
    onBackground = Color(0xFFEEE0D5),
    onSurface = Color(0xFFEEE0D5),
    outline = Color(0xFF9C8E84),
    surfaceVariant = Color(0xFF4F453D),
    onSurfaceVariant = Color(0xFFD2C4BB)
  )

private val OrangeLightColorScheme =
  lightColorScheme(
    primary = Color(0xFF944A00),
    secondary = Color(0xFFB12E00),
    tertiary = Color(0xFF755B00),
    background = Color(0xFFFFF8F5),
    surface = Color(0xFFFFFBFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    primaryContainer = Color(0xFFFFDDBE),
    onPrimaryContainer = Color(0xFF301400),
    secondaryContainer = Color(0xFFFFDBCF),
    onSecondaryContainer = Color(0xFF3B0900),
    onBackground = Color(0xFF211A15),
    onSurface = Color(0xFF211A15),
    outline = Color(0xFF85746A),
    surfaceVariant = Color(0xFFF4DED3),
    onSurfaceVariant = Color(0xFF52443C)
  )

// 3. BLUE Theme
private val BlueDarkColorScheme =
  darkColorScheme(
    primary = Color(0xFF9ECBFF),
    secondary = Color(0xFF72D2FF),
    tertiary = Color(0xFFC7BFFF),
    background = Color(0xFF0F141C),
    surface = Color(0xFF141924),
    onPrimary = Color(0xFF003362),
    onSecondary = Color(0xFF003548),
    onTertiary = Color(0xFF2E2478),
    primaryContainer = Color(0xFF004987),
    onPrimaryContainer = Color(0xFFD6E4FF),
    secondaryContainer = Color(0xFF004D67),
    onSecondaryContainer = Color(0xFFC2E8FF),
    onBackground = Color(0xFFE2E2E9),
    onSurface = Color(0xFFE2E2E9),
    outline = Color(0xFF8E9099),
    surfaceVariant = Color(0xFF43474E),
    onSurfaceVariant = Color(0xFFC3C6CF)
  )

private val BlueLightColorScheme =
  lightColorScheme(
    primary = Color(0xFF005FAF),
    secondary = Color(0xFF006689),
    tertiary = Color(0xFF5F52A7),
    background = Color(0xFFF8FAFF),
    surface = Color(0xFFFCFDFD),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    primaryContainer = Color(0xFFD6E4FF),
    onPrimaryContainer = Color(0xFF001C3B),
    secondaryContainer = Color(0xFFC2E8FF),
    onSecondaryContainer = Color(0xFF001E2C),
    onBackground = Color(0xFF1A1C1E),
    onSurface = Color(0xFF1A1C1E),
    outline = Color(0xFF74777F),
    surfaceVariant = Color(0xFFDFE2EB),
    onSurfaceVariant = Color(0xFF43474E)
  )

// 4. VIOLET Theme
private val VioletDarkColorScheme =
  darkColorScheme(
    primary = Color(0xFFD9B9FF),
    secondary = Color(0xFFFFB1C8),
    tertiary = Color(0xFF9CD5FF),
    background = Color(0xFF150F1E),
    surface = Color(0xFF1C1626),
    onPrimary = Color(0xFF461386),
    onSecondary = Color(0xFF5E1133),
    onTertiary = Color(0xFF023456),
    primaryContainer = Color(0xFF602E9F),
    onPrimaryContainer = Color(0xFFEEDCFF),
    secondaryContainer = Color(0xFF7C294A),
    onSecondaryContainer = Color(0xFFFFD9E2),
    onBackground = Color(0xFFE8E0EC),
    onSurface = Color(0xFFE8E0EC),
    outline = Color(0xFF968E9D),
    surfaceVariant = Color(0xFF4A4450),
    onSurfaceVariant = Color(0xFFCCC3D2)
  )

private val VioletLightColorScheme =
  lightColorScheme(
    primary = Color(0xFF7949B8),
    secondary = Color(0xFF983A60),
    tertiary = Color(0xFF006497),
    background = Color(0xFFFCF8FF),
    surface = Color(0xFFFFFBFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    primaryContainer = Color(0xFFEEDCFF),
    onPrimaryContainer = Color(0xFF29005A),
    secondaryContainer = Color(0xFFFFD9E2),
    onSecondaryContainer = Color(0xFF3E001D),
    onBackground = Color(0xFF1D1B20),
    onSurface = Color(0xFF1D1B20),
    outline = Color(0xFF7C7582),
    surfaceVariant = Color(0xFFEDE0F2),
    onSurfaceVariant = Color(0xFF4A4450)
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false, // Set to false to force our exquisite presidential custom theme
  themeStyle: ThemeStyle = ThemeStyle.GREEN,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      else -> {
        when (themeStyle) {
          ThemeStyle.GREEN -> if (darkTheme) GreenDarkColorScheme else GreenLightColorScheme
          ThemeStyle.ORANGE -> if (darkTheme) OrangeDarkColorScheme else OrangeLightColorScheme
          ThemeStyle.BLUE -> if (darkTheme) BlueDarkColorScheme else BlueLightColorScheme
          ThemeStyle.VIOLET -> if (darkTheme) VioletDarkColorScheme else VioletLightColorScheme
        }
      }
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
