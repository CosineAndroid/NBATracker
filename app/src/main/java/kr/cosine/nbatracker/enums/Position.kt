package kr.cosine.nbatracker.enums

enum class Position(
    val koreanName: String
) {
    ERROR("에러"),
    CENTER("센터"),
    FORWARD("포워드"),
    GUARD("가드");

    companion object {
        fun getPosition(positionText: String): Position {
            val split = positionText.split("-")
            return entries.find { it.name.first().toString() == split[0] } ?: ERROR
        }
    }
}