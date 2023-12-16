package xyz.starestarrysky.forex.server.manager.impl

import com.dukascopy.api.IOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import xyz.starestarrysky.forex.jforex.event.JForexEvent
import xyz.starestarrysky.forex.server.common.base.BizException
import xyz.starestarrysky.forex.server.model.StompModel
import xyz.starestarrysky.forex.server.property.EmailConfig
import xyz.starestarrysky.forex.server.property.StompConfig
import javax.mail.MessagingException

// TODO: encapsulate the notifications which email/sms/push...
@Service
class JForexEventImpl : JForexEvent {
    @Autowired
    private lateinit var stompConfig: StompConfig

    @Autowired
    private lateinit var messagingTemplate: SimpMessagingTemplate

    @Autowired
    private lateinit var emailConfig: EmailConfig

    @Autowired
    private lateinit var templateEngine: TemplateEngine

    @Autowired
    private lateinit var javaMailSender: JavaMailSender

    @Async
    override fun orderCreated(order: IOrder) {
        val context = Context().apply {
            this.setVariable("order", order)
        }
        val process = templateEngine.process("./mail/trade", context)

        val mimeMessage = javaMailSender.createMimeMessage()
        try {
            val mimeMessageHelper = MimeMessageHelper(mimeMessage, true).apply {
                this.setFrom(emailConfig.username)
                this.setTo(emailConfig.to)
                this.setSubject(order.label)
            }
            mimeMessageHelper.setText(process, true)
        } catch (e: MessagingException) {
            e.printStackTrace()
            throw BizException.builder().message(e.message).build()
        }
        javaMailSender.send(mimeMessage)

        pushBrowser(StompModel().apply {
            title = order.label
            body = "${order.instrument.name()} ${order.orderCommand.name} ${order.openPrice}"
        })
    }

    /**
     * cannot trade because of fused
     */
    @Async
    override fun fused() {
        pushBrowser(StompModel().apply {
            title = "fused"
            body = "fused !"
        })
    }

    private fun pushBrowser(stomp: StompModel) {
        messagingTemplate.convertAndSend("${stompConfig.broker}/forex-web", stomp)
    }
}
