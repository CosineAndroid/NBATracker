package kr.cosine.nbatracker.enums

enum class Conference(
    val koreanName: String
) {
    ALL("모두"),
    EAST("동부"),
    WEST("서부");

    companion object {
        val koreanNames = entries.map(Conference::koreanName)
    }
}