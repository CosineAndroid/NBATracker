package kr.cosine.nbatracker.data

import java.io.Serializable

data class PlayerStat(
    val points: Double,
    val rebound: Double,
    val assist: Double
) : Serializable
