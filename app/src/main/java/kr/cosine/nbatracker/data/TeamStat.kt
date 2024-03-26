package kr.cosine.nbatracker.data

import java.io.Serializable

data class TeamStat(
    val points: Double,
    val rebound: Double,
    val assist: Double,
    val againstPoints: Double
) : Serializable
