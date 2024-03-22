package kr.cosine.nbatracker.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kr.cosine.nbatracker.ui.theme.NBATrackerTheme

class TeamActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NBATrackerTheme {

            }
        }
    }
}