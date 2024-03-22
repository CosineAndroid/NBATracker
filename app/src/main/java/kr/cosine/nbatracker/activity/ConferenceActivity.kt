package kr.cosine.nbatracker.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kr.cosine.nbatracker.CustomText
import kr.cosine.nbatracker.Head
import kr.cosine.nbatracker.data.TeamInfo
import kr.cosine.nbatracker.enums.Conference
import kr.cosine.nbatracker.model.TeamInfoRegistry
import kr.cosine.nbatracker.ui.theme.NBATrackerTheme

class ConferenceActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NBATrackerTheme {
                Main()
            }
        }
    }
}

@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Main() {
    val pageState = rememberPagerState(
        initialPage = 0,
        pageCount = { Conference.koreanNames.size }
    )
    val coroutineScope = rememberCoroutineScope()

    Column {
        Head("컨퍼런스")
        SelectConference(pageState, coroutineScope)
        ConferenceTypeGuide()
        ScrollConferenceTeam(pageState)
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
                    CustomText(
                        text = title,
                        fontSize = 15.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
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
private fun ScrollConferenceTeam(pageState: PagerState) {
    HorizontalPager(
        state = pageState,
        userScrollEnabled = false
    ) { page ->
        LazyColumn {
            when (page) {
                0 -> itemsIndexed(TeamInfoRegistry.getTeamInfos()) { index, item ->
                    ConferenceTeam(index, item)
                }

                1 -> itemsIndexed(TeamInfoRegistry.getTeamInfosByConference(Conference.EAST)) { index, item ->
                    ConferenceTeam(index, item)
                }

                2 -> itemsIndexed(TeamInfoRegistry.getTeamInfosByConference(Conference.WEST)) { index, item ->
                    ConferenceTeam(index, item)
                }
            }
        }
    }
}

@Composable
private fun ConferenceTypeGuide() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color.White)
            .padding(
                horizontal = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CustomText(
            text = "순위",
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.08f)
        )
        CustomText(
            text = "팀",
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.30f)
        )
        CustomText(
            text = "승",
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.049f)
        )
        CustomText(
            text = "패",
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.048f)
        )
        CustomText(
            text = "승률",
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.068f)
        )
    }
}

@Composable
private fun ConferenceTeam(order: Int, teamInfo: TeamInfo) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(3.dp)
            .clickable {

            },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = 10.dp
            )
        ) {
            CustomText(
                text = String.format("%02d", order + 1),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .weight(0.035f)
                    .background(Color.Red)
            )
            AsyncImage(
                model = teamInfo.team.getModel(),
                contentDescription = teamInfo.team.koreanName,
                modifier = Modifier
                    .weight(0.08f)
                    .background(Color.Yellow)
            )
            CustomText(
                text = teamInfo.team.koreanName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .weight(0.45f)
                    .background(Color.Green),
            )
            CustomText(
                text = teamInfo.totalRecord.win,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.weight(0.07f)
            )
            CustomText(
                fontSize = 12.sp,
                text = teamInfo.totalRecord.lose,
                fontWeight = FontWeight.Light,
                modifier = Modifier.weight(0.07f)
            )
            CustomText(
                fontSize = 12.sp,
                text = teamInfo.totalRecord.rateText,
                fontWeight = FontWeight.Light,
                modifier = Modifier.weight(0.1f)
            )
        }
    }
}