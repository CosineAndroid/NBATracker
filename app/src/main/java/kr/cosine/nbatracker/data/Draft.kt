package kr.cosine.nbatracker.data

import java.io.Serializable

data class Draft(
    val year: Long? = null,
    val round: Long? = null,
    val number: Long? = null
) : Serializable {

    override fun toString(): String {
        if (year == null && round == null && number == null) {
            return "언드래프티"
        }
        return "${year}년 ${round}라운드 ${number}픽"
    }
}
