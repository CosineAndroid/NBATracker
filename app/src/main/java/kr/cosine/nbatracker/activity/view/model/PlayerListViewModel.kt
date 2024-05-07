package kr.cosine.nbatracker.activity.view.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerListViewModel : ViewModel() {

    private val _searchInputStateFlow = MutableStateFlow("")
    val searchInputStateFlow: StateFlow<String> = _searchInputStateFlow.asStateFlow()

    fun setSearchInput(input: String) {
        _searchInputStateFlow.value = input
    }
}