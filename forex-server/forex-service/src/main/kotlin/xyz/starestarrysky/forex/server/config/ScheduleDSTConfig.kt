package xyz.starestarrysky.forex.server.config

import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.CronTask
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.scheduling.support.CronTrigger
import org.springframework.scheduling.support.ScheduledMethodRunnable
import xyz.starestarrysky.forex.server.common.annotation.DSTScheduled
import xyz.starestarrysky.forex.server.schedule.ForexSchedule
import xyz.starestarrysky.forex.server.util.DSTUtil
import java.time.LocalTime
import java.time.ZoneId

@Configuration
class ScheduleDSTConfig : SchedulingConfigurer {
    @Autowired
    private lateinit var applicationContextConfig: ApplicationContextConfig

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        val isDSTNow = DSTUtil.isDST()

        taskRegistrar.setCronTasksList(taskRegistrar.cronTaskList.filter { cronTask ->
            cronTask.runnable is ScheduledMethodRunnable
                && (cronTask.runnable as ScheduledMethodRunnable).method.getDeclaredAnnotation(DSTScheduled::class.java) == null
        })

        val scan = ClassPathScanningCandidateComponentProvider(true)
        val components =
            scan.findCandidateComponents(ClassUtils.getPackageName(ForexSchedule::class.java))

        components.forEach { component ->
            Class.forName(component.beanClassName).declaredMethods.filter {
                it.getDeclaredAnnotationsByType(DSTScheduled::class.java).isNotEmpty()
                    && it.parameterCount == 0
            }.forEach { method ->
                val dstScheduled = method.getDeclaredAnnotation(DSTScheduled::class.java)

                taskRegistrar.addCronTask(CronTask({ method.invoke(applicationContextConfig.context?.getBean(method.declaringClass)) },
                    CronTrigger(calculateDST(isDSTNow, dstScheduled), ZoneId.of(dstScheduled.zone))))
            }
        }
    }

    private fun calculateDST(isDSTNow: Boolean, dstScheduled: DSTScheduled): String {
        val cronFields = StringUtils.split(dstScheduled.cron, ' ')
        if (cronFields.size < 3) {
            throw RuntimeException("the cron of @DSTScheduled is wrong.")
        }
        var cronTime = LocalTime.of(cronFields[2].toInt(), cronFields[1].toInt(), cronFields[0].toInt())
        when {
            isDSTNow and !dstScheduled.isCronDST -> cronTime = cronTime.minusHours(1)
            !isDSTNow and dstScheduled.isCronDST -> cronTime = cronTime.plusHours(1)
        }
        var newCron = cronTime.second.toString() + " " + cronTime.minute.toString() + " " + cronTime.hour.toString() + " "
        for ((index, cronField) in cronFields.withIndex()) {
            if (index >= 3) {
                newCron += "$cronField "
            }
        }
        return newCron.substring(0, newCron.length - 1)
    }
}
