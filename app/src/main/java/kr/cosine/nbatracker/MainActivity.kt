package kr.cosine.nbatracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.cosine.nbatracker.activity.ConferenceActivity
import kr.cosine.nbatracker.activity.PlayerListActivity
import kr.cosine.nbatracker.activity.view.model.LoadingViewModel
import kr.cosine.nbatracker.registry.PlayerInfoRegistry
import kr.cosine.nbatracker.registry.TeamInfoRegistry
import kr.cosine.nbatracker.service.TrackerService
import kr.cosine.nbatracker.ui.theme.Color
import kr.cosine.nbatracker.ui.theme.Font
import java.net.SocketTimeoutException

class MainActivity : ComponentActivity() {

    private val loadingViewModel: LoadingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingViewModel.show()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val playerInfoMap = TrackerService.getPlayerInfoMap()
                PlayerInfoRegistry.setPlayerInfoMap(playerInfoMap)

                val teamInfoMap = TrackerService.getTeamInfoMap()
                TeamInfoRegistry.setTeamInfoMap(teamInfoMap)
            } catch (e: SocketTimeoutException) {
                finish()
            }
        }.invokeOnCompletion {
            loadingViewModel.hide()
        }
        setContent {
            Main(
                loadingViewModel = loadingViewModel,
                buttonClickScope = this::startActivity
            )
        }
    }

    private fun startActivity(clazz: Class<out ComponentActivity>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }
}

@Composable
private fun Main(
    loadingViewModel: LoadingViewModel,
    buttonClickScope: (Class<out ComponentActivity>) -> Unit
) {
    val isLoading by loadingViewModel.isLoading.collectAsStateWithLifecycle()
    if (isLoading) {
        LoadingScreen()
    }
    MainCategoryButton(
        isLoading = isLoading,
        buttonClickScope = buttonClickScope
    )
}

@Composable
private fun LoadingScreen() {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        CircularProgressIndicator(
            color = Color.White,
            modifier = Modifier.clearAndSetSemantics {}
        )
    }
}

@Composable
private fun MainCategoryButton(
    isLoading: Boolean,
    buttonClickScope: (Class<out ComponentActivity>) -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.MainBackground)
    ) {
        CategoryButton(
            height = 0.5f,
            titleId = R.string.player,
            imageDrawableId = R.drawable.lebron_james,
            imageDescriptionId = R.string.main_player_image_description
        ) {
            if (isLoading) return@CategoryButton
            buttonClickScope(PlayerListActivity::class.java)
        }
        CategoryButton(
            height = 1f,
            titleId = R.string.conference,
            imageDrawableId = R.drawable.golden_state_warriors,
            imageDescriptionId = R.string.main_conference_image_description,
        ) {
            if (isLoading) return@CategoryButton
            buttonClickScope(ConferenceActivity::class.java)
        }
    }
}

@Composable
private fun CategoryButton(
    height: Float,
    titleId: Int,
    imageDrawableId: Int,
    imageDescriptionId: Int,
    clickScope: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(height)
            .padding(10.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                clickScope()
            },
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.CategoryButtonBackground)
        ) {
            Image(
                painter = painterResource(imageDrawableId),
                contentDescription = stringResource(imageDescriptionId),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
                    .background(Color.CategoryImageBackground)
            )
            Text(
                text = stringResource(titleId),
                fontSize = 70.sp,
                fontWeight = FontWeight.Medium,
                color = Color.CategoryText,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun Toolbar(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(
                    horizontal = 20.dp,
                    vertical = 15.dp
                )
        )
    }
}

@Composable
fun Text(
    modifier: Modifier = Modifier,
    text: Any,
    fontSize: TextUnit,
    textAlign: TextAlign? = null,
    fontWeight: FontWeight = FontWeight.Light,
    color: androidx.compose.ui.graphics.Color = Color.Black
) {
    var newFontSize by remember { mutableStateOf(fontSize) }
    androidx.compose.material3.Text(
        text = text.toString(),
        textAlign = textAlign,
        color = color,
        fontFamily = Font.esamanru,
        fontWeight = fontWeight,
        fontSize = newFontSize,
        modifier = modifier,
        softWrap = false,
        overflow = TextOverflow.Visible,
        onTextLayout = {
            if (it.didOverflowWidth) {
                newFontSize *= 0.95
            }
        }
    )
}

@Composable
fun Line(thickness: Dp = 2.dp) {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = thickness,
        color = Color.White
    )
}

@Composable
fun Space(width: Dp = 0.dp, height: Dp = 0.dp) {
    Spacer(
        modifier = Modifier
            .width(width)
            .height(height)
    )
}