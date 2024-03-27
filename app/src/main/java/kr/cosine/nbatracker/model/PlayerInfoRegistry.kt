package kr.cosine.nbatracker.model

import kr.cosine.nbatracker.data.PlayerInfo
import kr.cosine.nbatracker.enums.Team

object PlayerInfoRegistry {

    private var playerInfoMap = emptyMap<Long, PlayerInfo>()

    fun getPlayerInfos(): List<PlayerInfo> = playerInfoMap.values.toList()

    fun setPlayerInfoMap(playerInfoMap: Map<Long, PlayerInfo>) {
        this.playerInfoMap = playerInfoMap
    }

    fun getPlayerInfosByTeam(team: Team): List<PlayerInfo> {
        return playerInfoMap.values.filter { it.team == team }
    }
}