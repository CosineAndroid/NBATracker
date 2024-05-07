package kr.cosine.nbatracker.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kr.cosine.nbatracker.R
import kr.cosine.nbatracker.Text
import kr.cosine.nbatracker.activity.intent.IntentKey
import kr.cosine.nbatracker.activity.view.model.PlayerViewModel
import kr.cosine.nbatracker.data.PlayerInfo
import kr.cosine.nbatracker.data.TeamInfo
import kr.cosine.nbatracker.enums.Team
import kr.cosine.nbatracker.registry.PlayerInfoRegistry
import kr.cosine.nbatracker.registry.TeamInfoRegistry
import kr.cosine.nbatracker.ui.theme.Color

class TeamActivity : ComponentActivity() {

    @Suppress("deprecation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val team = intent.getSerializableExtra(IntentKey.TEAM) as? Team
        val teamInfo = if (team == null) {
            intent.getSerializableExtra(IntentKey.TEAM_INFO) as? TeamInfo
        } else {
            TeamInfoRegistry.findTeamInfo(team)
        } ?: run {
            Toast.makeText(this, "팀 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContent {
            Main(teamInfo)
        }
    }
}

@Composable
private fun Main(teamInfo: TeamInfo) {
    Column {
        TeamCard(teamInfo)
        TeamPlayers(teamInfo.team)
    }
}

@Composable
private fun TeamCard(teamInfo: TeamInfo) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = teamInfo.team.color
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
        modifier = Modifier.padding(
            horizontal = 5.dp
        )
    ) {
        TeamInfoImage(
            teamInfo = teamInfo,
            modifier = Modifier.weight(0.35f)
        )
        TeamInfoDescription(
            teamInfo = teamInfo,
            modifier = Modifier.weight(0.65f)
        )
    }
}

@Composable
private fun TeamInfoImage(teamInfo: TeamInfo, modifier: Modifier = Modifier) {
    val team = teamInfo.team
    AsyncImage(
        model = team.getModel(),
        contentDescription = team.shortName,
        modifier = modifier
    )
}

@Composable
private fun TeamInfoDescription(teamInfo: TeamInfo, modifier: Modifier = Modifier) {
    val team = teamInfo.team
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(
            horizontal = 5.dp
        )
    ) {
        Text(
            text = team.koreanName,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        val totalRecord = teamInfo.totalRecord
        Text(
            text = stringResource(R.string.team_win_and_lose, totalRecord.win, totalRecord.lose),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
        val ranking = TeamInfoRegistry.getRanking(team, team.conference) { it.totalRecord.rate }
        Text(
            text = stringResource(
                R.string.team_conference_rank,
                team.conference.koreanName,
                ranking
            ),
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
        TeamStatCard(stringResource(R.string.ppg), ppgRank, ppgStat, modifier)

        val oppgStat = teamStat.againstPoints
        val oppgRank =
            TeamInfoRegistry.getRanking(team, reverse = true) { it.teamStat.againstPoints }
        TeamStatCard(stringResource(R.string.team_oppg), oppgRank, oppgStat, modifier)

        val reboundStat = teamStat.rebound
        val reboundRank = TeamInfoRegistry.getRanking(team) { it.teamStat.rebound }
        TeamStatCard(stringResource(R.string.rebound), reboundRank, reboundStat, modifier)

        val assistStat = teamStat.assist
        val assistRank = TeamInfoRegistry.getRanking(team) { it.teamStat.assist }
        TeamStatCard(stringResource(R.string.assist), assistRank, assistStat, modifier)
    }
}

@Composable
private fun TeamStatCard(statName: String, rank: Int, stat: Double, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 5.dp
            )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = statName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(R.string.team_stat_rank, rank),
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
private fun TeamPlayers(
    team: Team,
    playerViewModel: PlayerViewModel = viewModel()
) {
    SimplePlayerTypeGuide()
    val popupPlayerInfo by playerViewModel.popupPlayerInfoStateFlow.collectAsStateWithLifecycle()
    val isPopup by playerViewModel.popupStateFlow.collectAsStateWithLifecycle()
    Box {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            userScrollEnabled = !isPopup,
            modifier = Modifier.background(Color.White)
        ) {
            itemsIndexed(
                items = PlayerInfoRegistry.getPlayerInfosByTeam(team),
                key = { id, _ -> id }
            ) { _, playerInfo ->
                SimplePlayerCard(
                    isPopup = isPopup,
                    playerInfo = playerInfo
                ) {
                    playerViewModel.setPopupPlayerInfo(playerInfo)
                    playerViewModel.setPopup(true)
                }
            }
        }
        PlayerCardPopup(
            isPopup = isPopup,
            playerInfo = popupPlayerInfo,
            modifier = Modifier.align(Alignment.Center),
            teamClickScope = {},
            closeClickScope = {
                playerViewModel.setPopup(false)
            },
            isTeamClickable = false
        )
    }
}

@Composable
fun SimplePlayerTypeGuide() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color.White)
            .padding(
                horizontal = 13.dp
            )
    ) {
        Text(
            text = stringResource(R.string.team_guide_player),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = stringResource(R.string.jersey_number),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.2f)
        )
        Text(
            text = stringResource(R.string.position),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.2f)
        )
    }
}

@Composable
fun SimplePlayerCard(
    isPopup: Boolean,
    playerInfo: PlayerInfo,
    playerClickScope: (PlayerInfo) -> Unit
) {
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
            .clickable(
                enabled = !isPopup
            ) {
                playerClickScope(playerInfo)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = playerInfo.fullName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = playerInfo.jerseyNumber,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.2f)
            )
            Text(
                text = playerInfo.position.koreanName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.2f)
            )
        }
    }
}