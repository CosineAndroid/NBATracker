package kr.cosine.nbatracker.registry

import kr.cosine.nbatracker.data.PlayerInfo
import kr.cosine.nbatracker.enums.Position
import kr.cosine.nbatracker.enums.Team

object PlayerInfoRegistry {
    private var playerInfoMap = emptyMap<Long, PlayerInfo>()

    fun getPlayerInfos(input: String = ""): List<PlayerInfo> {
        val word = input.lowercase()
        return playerInfoMap.values.filter {
            if (word.isEmpty()) return@filter true
            it.fullName.lowercase().contains(word) ||
                    it.team.isMatch(word) ||
                    it.position.koreanName.contains(word)
        }.toList()
    }

    fun setPlayerInfoMap(playerInfoMap: Map<Long, PlayerInfo>) {
        this.playerInfoMap = playerInfoMap
    }

    fun getPlayerInfosByTeam(team: Team): List<PlayerInfo> {
        return playerInfoMap.values.filter { it.team == team }.sortedBy { it.position }
    }

    fun getPlayerInfosByPosition(position: Position): List<PlayerInfo> {
        return playerInfoMap.values.filter { it.position == position }
    }
}