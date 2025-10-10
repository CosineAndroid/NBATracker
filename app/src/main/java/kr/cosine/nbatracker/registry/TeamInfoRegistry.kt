package kr.cosine.nbatracker.registry

import kr.cosine.nbatracker.data.TeamInfo
import kr.cosine.nbatracker.enums.Conference
import kr.cosine.nbatracker.enums.Team

object TeamInfoRegistry {
    private var teamInfoMap = mapOf<Team, TeamInfo>()

    fun findTeamInfo(team: Team): TeamInfo? {
        return teamInfoMap[team]
    }

    fun getTeamInfos(): List<TeamInfo> {
        return teamInfoMap.values.sortedByDescending { it.totalRecord.rate }
    }

    fun setTeamInfoMap(teamInfoMap: Map<Team, TeamInfo>) {
        this.teamInfoMap = teamInfoMap
    }

    fun getTeamInfosByConference(conference: Conference): List<TeamInfo> {
        return getTeamInfos().filter {
            conference == Conference.ALL || it.team.conference == conference
        }
    }

    fun getRanking(
        team: Team,
        conference: Conference = Conference.ALL,
        reverse: Boolean = false,
        filter: (TeamInfo) -> Double
    ): Int {
        var sortedEntries = teamInfoMap.entries.toList()
        if (conference != Conference.ALL) {
            sortedEntries = sortedEntries.filter {
                it.key.conference == conference
            }
        }
        sortedEntries = sortedEntries.sortedByDescending {
            filter(it.value)
        }
        if (reverse) {
            sortedEntries = sortedEntries.reversed()
        }
        return sortedEntries.indexOfFirst {
            it.key == team
        } + 1
    }
}