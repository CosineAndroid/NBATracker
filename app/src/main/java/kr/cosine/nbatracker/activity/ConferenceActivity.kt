package kr.cosine.nbatracker.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kr.cosine.nbatracker.R
import kr.cosine.nbatracker.Toolbar
import kr.cosine.nbatracker.Text
import kr.cosine.nbatracker.activity.intent.IntentKey
import kr.cosine.nbatracker.data.TeamInfo
import kr.cosine.nbatracker.enums.Conference
import kr.cosine.nbatracker.registry.TeamInfoRegistry
import kr.cosine.nbatracker.ui.theme.Color

class ConferenceActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main(this::openTeamActivity)
        }
    }

    private fun openTeamActivity(teamInfo: TeamInfo) {
        val intent = Intent(this, TeamActivity::class.java)
        intent.putExtra(IntentKey.TEAM_INFO, teamInfo)
        startActivity(intent)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Main(teamClickScope: (TeamInfo) -> Unit) {
    val pageState = rememberPagerState(
        initialPage = 0,
        pageCount = { Conference.koreanNames.size }
    )
    val coroutineScope = rememberCoroutineScope()
    Column {
        Toolbar(stringResource(R.string.conference))
        SelectConference(pageState, coroutineScope)
        ConferenceTypeGuide()
        ScrollConferenceTeam(pageState, teamClickScope)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SelectConference(pageState: PagerState, scope: CoroutineScope) {
    TabRow(
        selectedTabIndex = pageState.currentPage,
        containerColor = Color.Black,
        contentColor = Color.Yellow
    ) {
        Conference.koreanNames.forEachIndexed { index, title ->
            Tab(
                text = {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                },
                selected = pageState.currentPage == index,
                onClick = {
                    scope.launch {
                        pageState.scrollToPage(index)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScrollConferenceTeam(pageState: PagerState, teamClickScope: (TeamInfo) -> Unit) {
    HorizontalPager(pageState) { page ->
        LazyColumn(
            modifier = Modifier.background(Color.White)
        ) {
            val conference = Conference[page]
            itemsIndexed(TeamInfoRegistry.getTeamInfosByConference(conference)) { index, item ->
                ConferenceTeam(index, item, teamClickScope)
            }
        }
    }
}

@Composable
private fun ConferenceTypeGuide() {
    Row(
        modifier = Modifier
            .background(Color.White)
            .padding(
                horizontal = 13.dp,
                vertical = 5.dp
            )
    ) {
        Text(
            text = stringResource(R.string.conference_rank),
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.14f)
        )
        Text(
            text = stringResource(R.string.conference_team),
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.45f)
        )
        Text(
            text = stringResource(R.string.conference_win),
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.07f)
        )
        Text(
            text = stringResource(R.string.conference_lose),
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.07f)
        )
        Text(
            text = stringResource(R.string.conference_rate),
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.1f)
        )
    }
}

@Composable
private fun ConferenceTeam(
    order: Int,
    teamInfo: TeamInfo,
    teamClickScope: (TeamInfo) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        onClick = {
            teamClickScope(teamInfo)
        },
        modifier = Modifier.padding(3.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = 10.dp
            )
        ) {
            Text(
                text = String.format("%02d", order + 1),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.04f)
            )
            AsyncImage(
                model = teamInfo.team.getModel(),
                contentDescription = teamInfo.team.koreanName,
                modifier = Modifier
                    .weight(0.1f)
                    .padding(4.dp)
            )
            Text(
                text = teamInfo.team.koreanName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.weight(0.45f)
            )
            Text(
                text = teamInfo.totalRecord.win,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.07f)
            )
            Text(
                text = teamInfo.totalRecord.lose,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.07f)
            )
            Text(
                text = teamInfo.totalRecord.rateText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.1f)
            )
        }
    }
}