package kr.cosine.nbatracker.enums

enum class Conference(
    val koreanName: String
) {
    ALL("모두"),
    EAST("동부"),
    WEST("서부");

    companion object {
        val values = entries

        val koreanNames = entries.map(Conference::koreanName)

        operator fun get(index: Int): Conference = values[index]
    }
}