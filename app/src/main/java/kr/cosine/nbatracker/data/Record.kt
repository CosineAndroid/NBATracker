package kr.cosine.nbatracker.data

data class Record(
    val win: Long,
    val lose: Long
) {

    val total = win + lose

    val rate = win.toDouble() / total.toDouble()

    val rateText = "${rate}000".substring(0..4)

    operator fun plus(record: Record): Record {
        return Record(win + record.win, lose + record.lose)
    }
}