package xyz.starestarrysky.forex.server.common.annotation

import org.springframework.core.annotation.AliasFor
import org.springframework.scheduling.annotation.Scheduled

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
@Scheduled
annotation class DSTScheduled(
    @get:AliasFor(annotation = Scheduled::class, attribute = "cron")
    val cron: String = "",

    @get:AliasFor(annotation = Scheduled::class, attribute = "zone")
    val zone: String = "GMT",

    val isCronDST: Boolean = true
)
