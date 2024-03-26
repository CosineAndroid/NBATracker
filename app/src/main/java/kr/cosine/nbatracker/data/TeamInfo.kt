package kr.cosine.nbatracker.data

import kr.cosine.nbatracker.enums.Team
import java.io.Serializable

data class TeamInfo(
    val team: Team,
    val homeRecord: Record,
    val awayRecord: Record,
    val streak: String,
    val teamStat: TeamStat
) : Serializable {

    val totalRecord = homeRecord + awayRecord
}
