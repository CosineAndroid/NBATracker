package kr.cosine.nbatracker.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kr.cosine.nbatracker.CustomText
import kr.cosine.nbatracker.Line
import kr.cosine.nbatracker.Space
import kr.cosine.nbatracker.data.PlayerInfo
import kr.cosine.nbatracker.enums.Team
import kr.cosine.nbatracker.ui.theme.NBATrackerTheme
import kr.cosine.nbatracker.ui.theme.PlayerInfoGroupBackgroundColor
import kr.cosine.nbatracker.ui.theme.PlayerImageBackgroundColor
import kr.cosine.nbatracker.ui.theme.PlayerShortInfoBackgroundColor

class PlayerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playerInfo = intent.getSerializableExtra("PlayerInfo") as PlayerInfo

        setContent {
            NBATrackerTheme {
                PlayerInfoScreen(playerInfo, this::openTeamActivity)
            }
        }
    }

    private fun openTeamActivity(team: Team) {
        val intent = Intent(this, ConferenceActivity::class.java)
        intent.putExtra("TeamInfo", team) // 데이터 바꿔야함
        startActivity(intent)
    }
}

@Composable
private fun PlayerInfoScreen(playerInfo: PlayerInfo, clickTeamScope: (Team) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PlayerImage(playerInfo, clickTeamScope)
        Line()
        PlayerShortInfo(playerInfo)
        Line()
        PlayerInfoGroup(
            "드래프트" to playerInfo.draft
        )
        Line()
        val stat = playerInfo.playerStat
        PlayerInfoGroup(
            "득점" to stat.points,
            "리바운드" to stat.rebound,
            "어시스트" to stat.assist
        )
        Line()
        PlayerInfoGroup(
            "신장" to "${playerInfo.heightCentimeter} (${playerInfo.heightInchAndFeet})",
            "체중" to "${playerInfo.weightKilogram} (${playerInfo.weightPound})"
        )
        Line()
        PlayerInfoGroup(
            "도시" to playerInfo.country,
            "대학" to playerInfo.college
        )
        Line()
    }
}

@Composable
private fun PlayerImage(playerInfo: PlayerInfo, clickTeamScope: (Team) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .background(PlayerImageBackgroundColor)
    ) {
        val team = playerInfo.team
        AsyncImage(
            model = team.getModel(),
            contentDescription = team.fullName,
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(140.dp)
                .clickable {
                    clickTeamScope(playerInfo.team)
                }
        )
        AsyncImage(
            model = playerInfo.imageUrl,
            contentDescription = playerInfo.fullName,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun PlayerShortInfo(playerInfo: PlayerInfo) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(PlayerShortInfoBackgroundColor)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
        ) {
            CustomText(
                text = playerInfo.fullName,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Space(3.dp)
            CustomText(
                text = "${playerInfo.team.koreanName} ${playerInfo.jerseyNumber}번 ${playerInfo.position.koreanName}",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun PlayerInfoGroup(
    vararg infos: Pair<String, Any>
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(PlayerInfoGroupBackgroundColor)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            val isOne = infos.size == 1
            val titleSize = if (isOne) 16.sp else 12.sp
            val descriptionSize = if (isOne) 25.sp else 20.sp
            infos.forEach { info ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    CustomText(
                        text = info.first,
                        fontSize = titleSize,
                        color = Color.White
                    )
                    CustomText(
                        text = info.second.toString(),
                        fontSize = descriptionSize,
                        color = Color.White
                    )
                }
            }
        }
    }
}