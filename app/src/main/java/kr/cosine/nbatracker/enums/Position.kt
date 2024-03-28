package kr.cosine.nbatracker.enums

enum class Position(
    val koreanName: String
) {
    GUARD("가드"),
    FORWARD("포워드"),
    CENTER("센터");

    companion object {
        fun findPosition(positionText: String): Position? {
            val split = positionText.split("-")
            return entries.find { it.name.first().toString() == split[0] }
        }
    }
}