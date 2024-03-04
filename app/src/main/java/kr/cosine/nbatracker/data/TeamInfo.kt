package kr.cosine.nbatracker.data

import kr.cosine.nbatracker.enums.Team

data class TeamInfo(
    val team: Team,
    val totalRecord: Record,
    val homeRecord: Record,
    val awayRecord: Record,
    val streak: String,
    val teamStat: TeamStat
) {

}
