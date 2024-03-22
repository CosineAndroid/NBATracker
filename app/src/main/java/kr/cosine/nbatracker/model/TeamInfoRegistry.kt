package kr.cosine.nbatracker.model

import kr.cosine.nbatracker.data.Record
import kr.cosine.nbatracker.data.TeamInfo
import kr.cosine.nbatracker.data.TeamStat
import kr.cosine.nbatracker.enums.Conference
import kr.cosine.nbatracker.enums.Team

object TeamInfoRegistry {

    private var teamInfoMap = mapOf<Team, TeamInfo>(
        Team.ATLANTA_HAWKS to TeamInfo(
            Team.ATLANTA_HAWKS,
            Record(12, 13),
            Record(15, 11),
            "",
            TeamStat(1.0, 2.0, 3.0, 4.0)
        ),
        Team.GOLDEN_STATE_WARRIORS to TeamInfo(
            Team.GOLDEN_STATE_WARRIORS,
            Record(20, 10),
            Record(30, 20),
            "",
            TeamStat(54.0, 2.0, 8.0, 1.0)
        )
    )

    fun getTeamInfos(): List<TeamInfo> {
        return teamInfoMap.values.sortedByDescending { it.totalRecord.rate }
    }

    fun setTeamInfoMap(teamInfoMap: Map<Team, TeamInfo>) {
        this.teamInfoMap = teamInfoMap
    }

    fun getTeamInfosByConference(conference: Conference): List<TeamInfo> {
        return getTeamInfos().filter { it.team.conference == conference }
    }
}