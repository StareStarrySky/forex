package xyz.starestarrysky.forex.server.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DSTUtil {
    fun isDST(): Boolean {
        return isDST("GMT")
    }

    fun isDST(zone: String): Boolean {
        return isDST(zone, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }

    fun isDST(zone: String, isoLocalDateTime: String): Boolean {
        val zoneId = ZoneId.of(zone)
        val localDateTime = LocalDateTime.parse(isoLocalDateTime)
        val zonedDateTime = localDateTime.atZone(zoneId)
        val rules = zoneId.rules
        return rules.isDaylightSavings(zonedDateTime.toInstant())
    }
}
