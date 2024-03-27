package kr.cosine.nbatracker.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kr.cosine.nbatracker.Text
import kr.cosine.nbatracker.data.PlayerInfo
import kr.cosine.nbatracker.data.TeamInfo
import kr.cosine.nbatracker.enums.Team
import kr.cosine.nbatracker.model.PlayerInfoRegistry
import kr.cosine.nbatracker.model.TeamInfoRegistry
import kr.cosine.nbatracker.ui.theme.Color
import kr.cosine.nbatracker.ui.theme.NBATrackerTheme

class TeamActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val teamInfo = intent.getSerializableExtra("TeamInfo") as TeamInfo

        setContent {
            NBATrackerTheme {
                TeamView(teamInfo)
            }
        }
    }
}

@Composable
private fun TeamView(teamInfo: TeamInfo) {
    Column {
        TeamCard(teamInfo)
        TeamPlayerCard(teamInfo.team)
    }
}

@Composable
private fun TeamCard(teamInfo: TeamInfo) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        ),
        shape = RoundedCornerShape(
            bottomStart = 10.dp,
            bottomEnd = 10.dp
        )
    ) {
        Column {
            TeamInfo(teamInfo)
            TeamStat(teamInfo)
        }
    }
}

@Composable
private fun TeamInfo(teamInfo: TeamInfo) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        TeamInfoImage(teamInfo)
        TeamInfoDescription(teamInfo)
    }
}

@Composable
private fun TeamInfoImage(teamInfo: TeamInfo) {
    val team = teamInfo.team
    AsyncImage(
        model = team.getModel(),
        contentDescription = team.shortName,
        modifier = Modifier.size(140.dp)
    )
}

@Composable
private fun TeamInfoDescription(teamInfo: TeamInfo) {
    val team = teamInfo.team
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = team.koreanName,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        val totalRecord = teamInfo.totalRecord
        Text(
            text = "${totalRecord.win}승 ${totalRecord.lose}패",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
        val ranking = TeamInfoRegistry.getRanking(team, team.conference) { it.totalRecord.rate }
        Text(
            text = "${team.conference.koreanName}컨퍼런스 ${ranking}등",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Composable
private fun TeamStat(teamInfo: TeamInfo) {
    val team = teamInfo.team
    val teamStat = teamInfo.teamStat
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.padding(
            horizontal = 5.dp
        )
    ) {
        val modifier = Modifier.weight(1f)
        val ppgStat = teamStat.points
        val ppgRank = TeamInfoRegistry.getRanking(team) { it.teamStat.points }
        TeamStatCard("득점", ppgRank, ppgStat, modifier)

        val oppgStat = teamStat.againstPoints
        val oppgRank = TeamInfoRegistry.getRanking(team, reverse = true) { it.teamStat.againstPoints }
        TeamStatCard("실점", oppgRank, oppgStat, modifier)

        val reboundStat = teamStat.rebound
        val reboundRank = TeamInfoRegistry.getRanking(team) { it.teamStat.rebound }
        TeamStatCard("리바운드", reboundRank, reboundStat, modifier)

        val assistStat = teamStat.assist
        val assistRank = TeamInfoRegistry.getRanking(team) { it.teamStat.assist }
        TeamStatCard("어시스트", assistRank, assistStat, modifier)
    }
}

@Composable
private fun TeamStatCard(statName: String, rank: Int, stat: Double, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 5.dp
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = statName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${rank}등",
                fontSize = 23.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stat,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Composable
private fun TeamPlayerCard(team: Team) {
    TeamPlayerTypeGuide()
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(Color.White)
    ) {
        val teamPlayers = PlayerInfoRegistry.getPlayerInfosByTeam(team)
        itemsIndexed(teamPlayers) { _, playerInfo -> TeamPlayer(playerInfo) }
    }
}

@Composable
private fun TeamPlayerTypeGuide() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color.White)
            .padding(horizontal = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "선수",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "등번호",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.2f)
        )
        Text(
            text = "포지션",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.2f)
        )
    }
}

@Composable
private fun TeamPlayer(playerInfo: PlayerInfo) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(3.dp)
            /*.clickable {
                val intent = Intent(teamInstance, PlayerActivity::class.java)
                intent.putExtra("PlayerInfo", playerInfo)
                intent.putExtra("TeamImage", teamInfo.teamImage)
                teamInstance.startActivity(intent)
            },*/
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = playerInfo.fullName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = playerInfo.jerseyNumber,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.2f)
            )
            Text(
                text = playerInfo.position.koreanName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.2f)
            )
        }
    }
}
