package kr.cosine.nbatracker.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kr.cosine.nbatracker.CustomText
import kr.cosine.nbatracker.Head
import kr.cosine.nbatracker.data.PlayerInfo
import kr.cosine.nbatracker.manager.TrackerManager
import kr.cosine.nbatracker.ui.theme.Color
import kr.cosine.nbatracker.ui.theme.NBATrackerTheme

class PlayerListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NBATrackerTheme {
                Main(this::openPlayerActivity)
            }
        }
    }

    private fun openPlayerActivity(playerInfo: PlayerInfo) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra("PlayerInfo", playerInfo)
        startActivity(intent)
    }
}

@Composable
private fun Main(playerClickScope: (PlayerInfo) -> Unit) {
    Column {
        Head("선수")
        PlayerList(playerClickScope)
    }
}

@Composable
private fun PlayerList(playerClickScope: (PlayerInfo) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(TrackerManager.getPlayerInfos()) { _, item ->
            Player(item, playerClickScope)
        }
    }
}

@Composable
private fun Player(playerInfo: PlayerInfo, playerClickScope: (PlayerInfo) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(
                horizontal = 7.dp
            ).clickable {
                playerClickScope(playerInfo)
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            AsyncImage(
                model = playerInfo.imageUrl,
                contentDescription = playerInfo.fullName,
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .size(100.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomText(
                    text = playerInfo.fullName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                CustomText(
                    text = playerInfo.team.koreanName,
                    fontSize = 15.sp,
                    color = Color.TeamColor
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomText(
                            text = "등번호",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        CustomText(
                            text = playerInfo.jerseyNumber.toString(),
                            fontSize = 15.sp
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomText(
                            text = "포지션",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        CustomText(
                            text = playerInfo.position.koreanName,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
