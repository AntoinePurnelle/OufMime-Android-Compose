package net.ouftech.oufmime.ui.model

import androidx.compose.material3.ColorScheme
import net.ouftech.oufmime.ui.theme.TeamBlueColors
import net.ouftech.oufmime.ui.theme.TeamOrangeColors

enum class Team(val value: Int, val colors: ColorScheme) {
    Orange(0, TeamOrangeColors),
    Blue(1, TeamBlueColors),
}