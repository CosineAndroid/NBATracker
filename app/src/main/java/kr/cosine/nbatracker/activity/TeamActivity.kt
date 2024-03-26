package kr.cosine.nbatracker.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kr.cosine.nbatracker.Text
import kr.cosine.nbatracker.data.TeamInfo
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
        // TeamPlayerCard()
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
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TeamInfo(teamInfo)
            TeamStat(teamInfo)
        }
    }
}

@Composable
private fun TeamInfo(teamInfo: TeamInfo) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        val team = teamInfo.team
        AsyncImage(
            model = team.getModel(),
            contentDescription = team.shortName,
            modifier = Modifier.size(140.dp)
        )
        TeamInfoDescription(teamInfo)
    }
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
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val team = teamInfo.team
        val teamStat = teamInfo.teamStat
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val ppgStat = teamStat.points
            val ppgRank = TeamInfoRegistry.getRanking(team) { it.teamStat.points }
            TeamStatCard(width = 0.5f, statName = "득점", rank = ppgRank, stat = ppgStat)

            val oppgStat = teamStat.againstPoints
            val oppgRank = TeamInfoRegistry.getRanking(team, reverse = true) { it.teamStat.againstPoints }
            TeamStatCard(width = 1f, statName = "실점", rank = oppgRank, stat = oppgStat)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val reboundStat = teamStat.rebound
            val reboundRank = TeamInfoRegistry.getRanking(team) { it.teamStat.rebound }
            TeamStatCard(width = 0.5f, statName = "리바운드", rank = reboundRank, stat = reboundStat)

            val assistStat = teamStat.assist
            val assistRank = TeamInfoRegistry.getRanking(team) { it.teamStat.assist }
            TeamStatCard(width = 1f, statName = "어시스트", rank = assistRank, stat = assistStat)
        }
    }
}

@Composable
private fun TeamStatCard(width: Float, statName: String, rank: Int, stat: Double) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth(width)
            .padding(5.dp)
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

/*
@Composable
private fun TeamPlayerCard() {
    TeamPlayerGuide()
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val teamPlayers = tracker.getPlayerFromTeam(playerJson, teamInfo.teamEnglishShortName)
        itemsIndexed(teamPlayers) { _, item -> TeamPlayer(item) }
    }
}

@Composable
private fun TeamPlayerGuide() {
    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(30.size()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly)
    {
        Text(modifier = Modifier
            .fillMaxWidth(0.57f)
            .padding(start = 10.size()),
            text = "선수", fontFamily = esamanru, fontWeight = FontWeight.Medium)

        Text(modifier = Modifier.fillMaxWidth(0.4f),
            text = "등번호", fontFamily = esamanru, fontWeight = FontWeight.Medium)

        Text(modifier = Modifier.fillMaxWidth(1f),
            text = "포지션", fontFamily = esamanru, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun TeamPlayer(info: PlayerInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.size())
            .padding(5.size())
            .clickable {
                val intent = Intent(teamInstance, PlayerActivity::class.java)
                intent.putExtra("PlayerInfo", info)
                intent.putExtra("TeamImage", teamInfo.teamImage)
                teamInstance.startActivity(intent)
            },
        backgroundColor = Color.White,
        contentColor = Color.Black,
        elevation = 2.dp)
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(start = 10.size()),
                text = info.playerName, fontFamily = esamanru, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(modifier = Modifier.fillMaxWidth(0.4f),
                text = info.jerseyNumber, fontFamily = esamanru, fontWeight = FontWeight.Light)
            Text(modifier = Modifier.fillMaxWidth(1f),
                text = info.position, fontFamily = esamanru, fontWeight = FontWeight.Light)
        }
    }
}*/
