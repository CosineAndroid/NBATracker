package kr.cosine.nbatracker.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kr.cosine.nbatracker.CustomText
import kr.cosine.nbatracker.Head
import kr.cosine.nbatracker.data.TeamInfo
import kr.cosine.nbatracker.enums.Conference
import kr.cosine.nbatracker.manager.TrackerManager
import kr.cosine.nbatracker.ui.theme.NBATrackerTheme

class ConferenceActivity : ComponentActivity() {

    companion object {
        val conferencePages = listOf("모두", "동부", "서부")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NBATrackerTheme {
                Main()
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Main() {
    val pageState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column {
        Head("컨퍼런스")
        SelectConference(pageState, coroutineScope)
        ScrollConferenceTeam(pageState)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun SelectConference(pageState: PagerState, scope: CoroutineScope) {
    TabRow(
        selectedTabIndex = pageState.currentPage,
        indicator = { TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pageState, it)) },
        backgroundColor = Color.Black,
        contentColor = Color.Yellow
    ) {
        ConferenceActivity.conferencePages.forEachIndexed { index, title ->
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

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ScrollConferenceTeam(pageState: PagerState) {
    HorizontalPager(count = ConferenceActivity.conferencePages.size, state = pageState) { page ->
        LazyColumn {
            when (page) {
                0 -> itemsIndexed(TrackerManager.getTeamInfos()) { index, item ->
                    ConferenceTeam(index, item)
                }

                1 -> itemsIndexed(TrackerManager.getTeamInfosByConference(Conference.EAST)) { index, item ->
                    ConferenceTeam(index, item)
                }

                2 -> itemsIndexed(TrackerManager.getTeamInfosByConference(Conference.WEST)) { index, item ->
                    ConferenceTeam(index, item)
                }
            }
        }
    }
}

@Composable
private fun ConferenceTeam(order: Int, teamInfo: TeamInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(2.dp)
            .clickable {

            },
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        Row(
            //horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomText(
                text = String.format("%02d", order + 1),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .weight(0.5f)
            )
            AsyncImage(
                model = teamInfo.team.getModel(),
                contentDescription = teamInfo.team.koreanName,
                modifier = Modifier
                    .weight(0.1f)
                    .size(40.dp, 40.dp)
            )
            CustomText(
                text = teamInfo.team.koreanName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .weight(0.4f),
            )
            CustomText(
                fontSize = 12.sp,
                text = teamInfo.totalRecord.total.toString(),
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .weight(0.05f)
            )
            CustomText(
                text = teamInfo.totalRecord.win.toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .weight(0.05f)
            )
            CustomText(
                fontSize = 12.sp,
                text = teamInfo.totalRecord.lose.toString(),
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .weight(0.05f)
            )
        }
    }
}