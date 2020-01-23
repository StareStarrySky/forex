package xyz.starestarrysky.forex.jforex.strategy

import com.dukascopy.api.*
import org.slf4j.LoggerFactory
import xyz.starestarrysky.forex.base.biz.api.ForexControl
import xyz.starestarrysky.forex.jforex.entity.ConfigSetting
import xyz.starestarrysky.forex.jforex.platform.JForexPlatform
import xyz.starestarrysky.forex.jforex.entity.OpenOrder
import xyz.starestarrysky.forex.jforex.event.JForexEvent

/**
 * 全ての存在は灭びるようにデザインされている。
 *
 * 生と死を缲り返す螺旋に･･･
 *
 * 私达は囚われ続けている。
 *
 * これは、呪いか。
 *
 * それとも、罚か。
 *
 * 不可解なパズルを渡した神に
 *
 * いつか、私达は弓を引くのだろうか？
 */
class NeverEndingSpiral(
    private val jForexPlatform: JForexPlatform,
    private val openOrder: OpenOrder,
    private val configSettings: List<ConfigSetting>,
    private val neverEndingSpiralEd: NeverEndingSpiralEd,
    private val jForexEvent: JForexEvent?
) : IStrategy, ForexControl {
    companion object {
        private const val LOGGER_LINE_PREFIX = "策略日志："
        private const val LOGGER_PREFIX = "【策略日志开始】"
        private const val LOGGER_SUFFIX = "【策略日志结束】"
        private val LOGGER = LoggerFactory.getLogger(NeverEndingSpiral::class.java)
    }

    override fun onStart(context: IContext) {
        LOGGER.warn("${LOGGER_LINE_PREFIX}策略启动")

        jForexPlatform.iContext = context
        jForexPlatform.iEngine = context.engine

        neverEndingSpiralEd.init(jForexPlatform, openOrder, jForexEvent)
    }

    override fun onTick(instrument: Instrument, tick: ITick) {

    }

    override fun onBar(instrument: Instrument, period: Period, askBar: IBar, bidBar: IBar) {
        for (configSetting in configSettings) {
            if (instrument != configSetting.instrument || period != configSetting.smallPeriod) {
                continue
            }
            neverEndingSpiralEd.onBar(configSetting)
        }
    }

    override fun onMessage(message: IMessage) {
        if (message.type != IMessage.Type.INSTRUMENT_STATUS) {
            LOGGER.info(LOGGER_PREFIX)
            LOGGER.info("消息")
            LOGGER.info("类型 - ${message.type.name}")
            LOGGER.info("货币 - ${message.order?.instrument?.name}")
            LOGGER.info("内容 - ${message.content}")
            LOGGER.info("原因 - ${(message.reasons.map { it.name }).joinToString()}")
            LOGGER.info(LOGGER_SUFFIX)
        }
    }

    override fun onAccount(account: IAccount) {
        if (account.accountState.name != IAccount.AccountState.OK.name) {
            LOGGER.info("${LOGGER_LINE_PREFIX}账户 | 账户状态 - ${account.accountState.name}")
        }
    }

    override fun onStop() {
        LOGGER.warn("${LOGGER_LINE_PREFIX}策略停止")

        jForexPlatform.iContext.stop()
    }
}
