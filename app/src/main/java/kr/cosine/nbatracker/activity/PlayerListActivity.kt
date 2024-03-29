package kr.cosine.nbatracker.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kr.cosine.nbatracker.Text
import kr.cosine.nbatracker.Head
import kr.cosine.nbatracker.Line
import kr.cosine.nbatracker.Space
import kr.cosine.nbatracker.data.PlayerInfo
import kr.cosine.nbatracker.enums.Team
import kr.cosine.nbatracker.model.PlayerInfoRegistry
import kr.cosine.nbatracker.ui.theme.Color
import kr.cosine.nbatracker.ui.theme.Font
import kr.cosine.nbatracker.ui.theme.NBATrackerTheme

class PlayerListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NBATrackerTheme {
                Main(
                    teamClickScope = this::openTeamActivity
                )
            }
        }
    }

    private fun openTeamActivity(team: Team) {

    }
}

@Composable
private fun Main(
    playerViewModel: PlayerViewModel = viewModel(),
    playerListViewModel: PlayerListViewModel = viewModel(),
    teamClickScope: (Team) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Head("선수")
        PlayerSearchBar(playerViewModel, playerListViewModel)
        PlayerCardList(playerViewModel, playerListViewModel, teamClickScope)
    }
}

@Composable
private fun PlayerSearchBar(
    playerViewModel: PlayerViewModel,
    playerListViewModel: PlayerListViewModel
) {
    val isPopup = playerViewModel.popupStateFlow.collectAsState().value
    val searchInput = playerListViewModel.searchInputStateFlow.collectAsState().value
    BasicTextField(
        enabled = !isPopup,
        value = searchInput,
        onValueChange = playerListViewModel::setSearchInput,
        textStyle = TextStyle(
            fontSize = 18.sp,
            fontFamily = Font.esamanru,
            fontWeight = FontWeight.Light
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.SearchBar,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색",
                    tint = Color.SearchBarElement,
                    modifier = Modifier.padding(5.dp)
                )
                Space(width = 2.dp)
                if (searchInput.isEmpty()) {
                    Text(
                        text = "검색 (이름/팀/포지션)",
                        fontSize = 16.sp,
                        color = Color.SearchBarElement
                    )
                } else {
                    innerTextField()
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.07f)
            .background(Color.White)
            .padding(7.dp)
    )
}

@Composable
private fun PlayerCardList(
    playerViewModel: PlayerViewModel,
    playerListViewModel: PlayerListViewModel,
    teamClickScope: (Team) -> Unit
) {
    val searchInput = playerListViewModel.searchInputStateFlow.collectAsState().value
    val popupPlayerInfo = playerViewModel.popupPlayerInfoStateFlow.collectAsState().value
    val isPopup = playerViewModel.popupStateFlow.collectAsState().value
    Box {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = !isPopup,
            modifier = Modifier
                .fillMaxHeight()
                .background(Color.White)
        ) {
            item { Space(height = 1.dp) }
            itemsIndexed(PlayerInfoRegistry.getPlayerInfos(searchInput)) { _, playerInfo ->
                PlayerCard(
                    isPopup = isPopup,
                    playerInfo = playerInfo
                ) {
                    playerViewModel.setPopupPlayerInfo(playerInfo)
                    playerViewModel.setPopup(true)
                }
            }
            item { Space(height = 1.dp) }
        }
        PlayerCardPopup(
            isPopup = isPopup,
            playerInfo = popupPlayerInfo,
            modifier = Modifier.align(Alignment.Center),
            teamClickScope = teamClickScope,
            closeClickScope = {
                playerViewModel.setPopup(false)
            }
        )
    }
}

@Composable
private fun PlayerCard(
    isPopup: Boolean,
    playerInfo: PlayerInfo,
    playerClickScope: () -> Unit
) {
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
            )
            .clickable(
                enabled = !isPopup
            ) {
                playerClickScope()
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            PlayerCardImage(playerInfo)
            PlayerCardInfo(playerInfo)
        }
    }
}

@Composable
private fun PlayerCardImage(playerInfo: PlayerInfo) {
    AsyncImage(
        model = playerInfo.imageUrl,
        contentDescription = playerInfo.fullName,
        modifier = Modifier
            .fillMaxWidth(0.3f)
            .size(100.dp)
    )
}

@Composable
private fun PlayerCardInfo(playerInfo: PlayerInfo) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        PlayerCardFullName(playerInfo)
        PlayerCardTeamKoreanName(playerInfo)
        PlayerCardDescriptions(playerInfo)
    }
}

@Composable
private fun PlayerCardFullName(playerInfo: PlayerInfo) {
    Text(
        text = playerInfo.fullName,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun PlayerCardTeamKoreanName(playerInfo: PlayerInfo) {
    Text(
        text = playerInfo.team.koreanName,
        fontSize = 15.sp,
        color = Color.Team
    )
}

@Composable
private fun PlayerCardDescriptions(playerInfo: PlayerInfo) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        PlayerCardDescription(
            title = "등번호",
            description = playerInfo.jerseyNumber
        )
        PlayerCardDescription(
            title = "포지션",
            description = playerInfo.position.koreanName
        )
    }
}

@Composable
private fun PlayerCardDescription(
    title: Any,
    description: Any
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 10.sp,
            color = Color.Gray
        )
        Text(
            text = description,
            fontSize = 15.sp
        )
    }
}

@Composable
fun PlayerCardPopup(
    isPopup: Boolean,
    playerInfo: PlayerInfo?,
    modifier: Modifier = Modifier,
    teamClickScope: (Team) -> Unit,
    closeClickScope: () -> Unit
) {
    AnimatedVisibility(
        visible = isPopup,
        enter = fadeIn(tween(200, easing = LinearOutSlowInEasing)),
        exit = fadeOut(tween(200, easing = LinearOutSlowInEasing)),
        modifier = modifier
    ) {
        if (playerInfo == null) return@AnimatedVisibility
        val roundedCornershape = RoundedCornerShape(30.dp)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = roundedCornershape
                )
                .padding(1.dp)
                .clip(roundedCornershape)
        ) {
            PlayerImage(playerInfo, teamClickScope, closeClickScope)
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
        }
    }
}

@Composable
private fun PlayerImage(
    playerInfo: PlayerInfo,
    teamClickScope: (Team) -> Unit,
    closeClickScope: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .background(playerInfo.team.color)
    ) {
        val team = playerInfo.team
        AsyncImage(
            model = team.getModel(),
            contentDescription = team.englishName,
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(120.dp)
                .clickable {
                    teamClickScope(playerInfo.team)
                }
        )
        AsyncImage(
            model = playerInfo.imageUrl,
            contentDescription = playerInfo.fullName,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "닫기",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(70.dp)
                .padding(20.dp)
                .clickable {
                    closeClickScope()
                }
        )
    }
}

@Composable
private fun PlayerShortInfo(playerInfo: PlayerInfo) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.PlayerShortInfoBackground)

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = playerInfo.fullName,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Space(height = 3.dp)
            Text(
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
            .fillMaxWidth()
            .background(Color.PlayerInfoGroupBackground)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            val isOne = infos.size == 1
            val titleSize = if (isOne) 16.sp else 13.sp
            val descriptionSize = if (isOne) 25.sp else 20.sp
            infos.forEach { info ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = info.first,
                        fontSize = titleSize,
                        color = Color.White
                    )
                    Text(
                        text = info.second.toString(),
                        fontSize = descriptionSize,
                        color = Color.White
                    )
                }
            }
        }
    }
}