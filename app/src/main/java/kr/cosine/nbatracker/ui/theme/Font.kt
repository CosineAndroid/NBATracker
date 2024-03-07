package kr.cosine.nbatracker.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import kr.cosine.nbatracker.R

object Font {

    val esamanru = FontFamily(
        Font(R.font.esamanru_light, FontWeight.Light, FontStyle.Normal),
        Font(R.font.esamanru_medium, FontWeight.Medium, FontStyle.Normal),
        Font(R.font.esamanru_bold, FontWeight.Bold, FontStyle.Normal),
    )
}