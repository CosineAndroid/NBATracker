package kr.cosine.nbatracker.manager

import kr.cosine.nbatracker.data.PlayerInfo
import kr.cosine.nbatracker.data.TeamInfo
import kr.cosine.nbatracker.enums.Conference
import kr.cosine.nbatracker.enums.Team

object TrackerManager {

    private var playerInfoMap = mapOf<Long, PlayerInfo>()

    private var teamInfoMap = mapOf<Team, TeamInfo>()

    fun getPlayerInfos(): List<PlayerInfo> = playerInfoMap.values.toList()

    fun setPlayerInfoMap(playerInfoMap: Map<Long, PlayerInfo>) {
        this.playerInfoMap = playerInfoMap
    }

    fun getTeamInfos(): List<TeamInfo> = teamInfoMap.values.sortedByDescending { it.totalRecord.rate }

    fun setTeamInfoMap(teamInfoMap: Map<Team, TeamInfo>) {
        this.teamInfoMap = teamInfoMap
    }

    fun getTeamInfosByConference(conference: Conference): List<TeamInfo> {
        return getTeamInfos().filter { it.team.conference == conference }
    }
}