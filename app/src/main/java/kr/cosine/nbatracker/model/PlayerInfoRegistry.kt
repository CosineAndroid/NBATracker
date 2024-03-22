package kr.cosine.nbatracker.model

import kr.cosine.nbatracker.data.PlayerInfo

object PlayerInfoRegistry {

    private var playerInfoMap = emptyMap<Long, PlayerInfo>()

    fun getPlayerInfos(): List<PlayerInfo> = playerInfoMap.values.toList()

    fun setPlayerInfoMap(playerInfoMap: Map<Long, PlayerInfo>) {
        this.playerInfoMap = playerInfoMap
    }
}