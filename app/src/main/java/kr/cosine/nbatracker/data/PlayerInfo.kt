package kr.cosine.nbatracker.data

import kr.cosine.nbatracker.enums.Position
import kr.cosine.nbatracker.enums.Team
import kr.cosine.nbatracker.util.FormatUtil.toCentimeter
import kr.cosine.nbatracker.util.FormatUtil.toKilogram
import java.io.Serializable

data class PlayerInfo(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val jerseyNumber: Int,
    val position: Position,
    val team: Team,
    private val height: String?,
    private val weight: String?,
    val draft: Draft,
    val playerStat: PlayerStat,
    private val baseCountry: String?,
    private val baseCollege: String?
) : Serializable {
    val imageUrl = PLAYER_IMAGE_URL.format(id)

    val fullName = "$firstName $lastName"
    val shortName = "${firstName.first()}. $lastName"

    val fullOrShortName = if (fullName.length > MAX_NAME_SIZE) {
        fullName.slice(0..MAX_NAME_SIZE - 2) + "..."
    } else {
        fullName
    }

    val heightCentimeter by lazy { if (height == null) "측정되지 않음" else "${height.toCentimeter()}cm" }

    val weightKilogram by lazy { if (weight == null) "측정되지 않음" else "${weight.toKilogram()}kg" }

    val country = baseCountry ?: "불러오지 못함"

    val college = baseCollege ?: "불러오지 못함"

    private companion object {
        const val PLAYER_IMAGE_URL = "https://cdn.nba.com/headshots/nba/latest/1040x760/%d.png"
        const val MAX_NAME_SIZE = 22
    }
}