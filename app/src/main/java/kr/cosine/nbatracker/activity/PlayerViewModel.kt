package kr.cosine.nbatracker.activity

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.cosine.nbatracker.data.PlayerInfo

class PlayerViewModel : ViewModel() {

    private val _popupPlayerInfoStateFlow = MutableStateFlow<PlayerInfo?>(null)
    val popupPlayerInfoStateFlow: StateFlow<PlayerInfo?> = _popupPlayerInfoStateFlow.asStateFlow()

    private val _popupStateFlow = MutableStateFlow(false)
    val popupStateFlow: StateFlow<Boolean> = _popupStateFlow.asStateFlow()

    fun setPopupPlayerInfo(playerInfo: PlayerInfo?) {
        _popupPlayerInfoStateFlow.value = playerInfo
    }

    fun setPopup(isVisible: Boolean) {
        _popupStateFlow.value = isVisible
    }
}