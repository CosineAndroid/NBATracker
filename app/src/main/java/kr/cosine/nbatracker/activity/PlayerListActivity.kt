package kr.cosine.nbatracker.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kr.cosine.nbatracker.Text
import kr.cosine.nbatracker.Head
import kr.cosine.nbatracker.Space
import kr.cosine.nbatracker.data.PlayerInfo
import kr.cosine.nbatracker.model.PlayerInfoRegistry
import kr.cosine.nbatracker.ui.theme.Color
import kr.cosine.nbatracker.ui.theme.Font
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Head("선수")
        PlayerSearchBar()
        PlayerCardList(playerClickScope)
    }
}

@Composable
private fun PlayerSearchBar(viewModel: PlayerListViewModel = viewModel()) {
    val searchInput = viewModel.searchInputStateFlow.collectAsState().value
    BasicTextField(
        value = searchInput,
        onValueChange = viewModel::setSearchInput,
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
                    ).padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "",
                    tint = Color.SearchBarElement,
                )
                Space(width = 8.dp)
                if (searchInput.isEmpty()) {
                    Text(
                        text = "검색",
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
            .padding(7.dp)
    )
}

@Composable
private fun PlayerCardList(playerClickScope: (PlayerInfo) -> Unit, viewModel: PlayerListViewModel = viewModel()) {
    val searchInput = viewModel.searchInputStateFlow.collectAsState().value
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.White)
    ) {
        itemsIndexed(PlayerInfoRegistry.getPlayerInfos(searchInput)) { _, item ->
            PlayerCard(item, playerClickScope)
        }
    }
}

@Composable
private fun PlayerCard(playerInfo: PlayerInfo, playerClickScope: (PlayerInfo) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        onClick = {
            playerClickScope(playerInfo)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(
                horizontal = 7.dp
            )
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
                Text(
                    text = playerInfo.fullName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = playerInfo.team.koreanName,
                    fontSize = 15.sp,
                    color = Color.Team
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "등번호",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = playerInfo.jerseyNumber.toString(),
                            fontSize = 15.sp
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "포지션",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = playerInfo.position.koreanName,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
