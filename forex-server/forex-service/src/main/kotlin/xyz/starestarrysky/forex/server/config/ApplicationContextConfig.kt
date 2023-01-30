package xyz.starestarrysky.forex.server.config

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationContextConfig : ApplicationContextAware {
    var context: ApplicationContext? = null

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.context = applicationContext
    }
}
