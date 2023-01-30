package xyz.starestarrysky.forex.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import xyz.starestarrysky.forex.server.forex.property.JForexConfig
import xyz.starestarrysky.forex.server.property.EmailConfig

@EnableAsync
@EnableCaching
@EnableScheduling
@EnableConfigurationProperties(JForexConfig::class, EmailConfig::class)
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class, HibernateJpaAutoConfiguration::class])
class ForexServerApplication

fun main(args: Array<String>) {
    runApplication<ForexServerApplication>(*args)
}
