package kr.cosine.nbatracker.util

object FormatUtil {

    fun String.toCentimeter(): Double {
        val split = this.split("-")
        val feat = split[0].toInt()
        val inch = split[1].toInt()
        return ((feat * 30.48) + (inch * 2.54)).format()
    }

    fun String.toKilogram(): Double {
        return (this.toInt() * 0.453592).format()
    }

    private fun Number.format(): Double {
        return String.format("%.1f", this).toDouble()
    }
}