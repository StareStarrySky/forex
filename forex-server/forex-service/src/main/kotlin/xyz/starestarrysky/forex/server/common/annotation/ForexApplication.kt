package xyz.starestarrysky.forex.server.common.annotation

import org.springframework.core.annotation.AliasFor
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
@Order
annotation class ForexApplication(
    @get:AliasFor(annotation = Component::class, attribute = "value")
    val component: String = "",

    @get:AliasFor(annotation = Order::class, attribute = "value")
    val order: Int = 0
)
