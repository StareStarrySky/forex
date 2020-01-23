package xyz.starestarrysky.forex.server.common.base

import org.slf4j.LoggerFactory
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletResponse

open class BaseRest {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(BaseRest::class.java)
    }

    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun uncaughtExceptionHandle(e: Exception, response: HttpServletResponse): RestMessage {
        val result = RestMessage("501", e.message!!)
        response.status = 500
        when (e) {
            is BizException -> {
                LOGGER.warn(e.message)
                response.status = e.httpStatus
                result.code = e.code
            }
            is MethodArgumentNotValidException -> {
                val errors = e.bindingResult.allErrors
                var message = ""
                for (err in errors) {
                    message += "${err.defaultMessage}！"
                }
                LOGGER.warn(message)
                result.message = message
            }
            is BindException -> {
                val errors = e.bindingResult.allErrors
                var message = "验证错误，共有" + errors.size + "个错误：<br/>\n"
                for (err in errors) {
                    message += "${err.defaultMessage}<br/>\n"
                }
                LOGGER.warn(message)
                result.message = message
            }
        }
        return result
    }
}
