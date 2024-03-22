package kr.cosine.nbatracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.cosine.nbatracker.activity.PlayerListActivity
import kr.cosine.nbatracker.activity.ConferenceActivity
import kr.cosine.nbatracker.model.PlayerInfoRegistry
import kr.cosine.nbatracker.model.TeamInfoRegistry
import kr.cosine.nbatracker.service.TrackerService
import kr.cosine.nbatracker.ui.theme.Font
import kr.cosine.nbatracker.ui.theme.NBATrackerTheme
import kotlin.coroutines.CoroutineContext

class MainActivity : ComponentActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch {
            runBlocking {
                val playerInfoMap = TrackerService.getPlayerInfoMap()
                PlayerInfoRegistry.setPlayerInfoMap(playerInfoMap)

                val teamInfoMap = TrackerService.getTeamInfoMap()
                TeamInfoRegistry.setTeamInfoMap(teamInfoMap)
            }
        }
        setContent {
            NBATrackerTheme {
                Column {
                    MainButton(height = 0.5f, value = "선수") {
                        val intent = Intent(this@MainActivity, PlayerListActivity::class.java)
                        startActivity(intent)
                    }
                    MainButton(height = 1f, value = "컨퍼런스") {
                        val intent = Intent(this@MainActivity, ConferenceActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}

@Composable
private fun MainButton(height: Float, value: String, clickScope: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxHeight(height)
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                clickScope()
            },
        shape = RoundedCornerShape(10.dp)
    ) {
        CustomText(
            text = value,
            fontSize = 70.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
        )
    }
}


@Composable
fun Head(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        CustomText(
            text = text,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(
                    horizontal = 20.dp,
                    vertical = 15.dp
                )
        )
    }
}

@Composable
fun CustomText(
    modifier: Modifier = Modifier,
    text: Any,
    fontSize: TextUnit,
    textAlign: TextAlign? = null,
    fontWeight: FontWeight = FontWeight.Light,
    color: Color = Color.Black
) {
    var newFontSize by remember { mutableStateOf(fontSize) }
    Text(
        text = text.toString(),
        textAlign = textAlign,
        color = color,
        fontFamily = Font.esamanru,
        fontWeight = fontWeight,
        fontSize = newFontSize,
        modifier = modifier,
        softWrap = false,
        overflow = TextOverflow.Visible,
        onTextLayout = {
            if (it.didOverflowWidth) {
                newFontSize *= 0.95
            }
        }
    )
}

@Composable
fun Line(thickness: Dp = 2.dp) {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = thickness,
        color = Color.White
    )
}

@Composable
fun Space(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}




