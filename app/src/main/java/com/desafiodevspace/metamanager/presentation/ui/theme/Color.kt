package com.desafiodevspace.metamanager.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// --- NOVA PALETA DE CORES (MODO CLARO E ESCURO) ---

// Paleta Principal (Modo Claro)
val Primary = Color(0xFF006C4C)
val OnPrimary = Color.White
val Secondary = Color(0xFF4DDAC1)
val OnSecondary = Color.Black
val Background = Color(0xFFF7F7F7)
val Surface = Color.White
val OnBackground = Color(0xFF1A1C1B)
val OnSurface = Color(0xFF1A1C1B)
val Error = Color(0xFFB00020)

// Paleta do Modo Escuro
val DarkPrimary = Color(0xFF55DBC2)
val DarkOnPrimary = Color(0xFF003827)
val DarkSecondary = Color(0xFF4DDAC1)
val DarkOnSecondary = Color(0xFF003827)
val DarkBackground = Color(0xFF121829)
val DarkSurface = Color(0xFF1A2033)
val DarkOnBackground = Color(0xFFE0E3E1)
val DarkOnSurface = Color(0xFFE0E3E1)
val DarkError = Color(0xFFCF6679)


// --- CAMADA DE COMPATIBILIDADE PARA CORES ANTIGAS ---
// Isso corrige os erros "Unresolved reference" nos componentes existentes.

val CardBackground: Color @Composable get() = MaterialTheme.colorScheme.surface
val AnalyticsCardBackground: Color @Composable get() = MaterialTheme.colorScheme.surface
val LightText: Color @Composable get() = MaterialTheme.colorScheme.onSurface
val SecondaryText: Color @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
val PrimaryTeal: Color @Composable get() = MaterialTheme.colorScheme.primary
val CheckboxChecked: Color @Composable get() = MaterialTheme.colorScheme.primary
val CircularProgressFill: Color @Composable get() = MaterialTheme.colorScheme.primary
val ProgressFill: Color @Composable get() = MaterialTheme.colorScheme.primary
val CircularProgressTrack: Color @Composable get() = MaterialTheme.colorScheme.surfaceVariant
val ProgressTrack: Color @Composable get() = MaterialTheme.colorScheme.surfaceVariant
val DividerColor: Color @Composable get() = MaterialTheme.colorScheme.outline
