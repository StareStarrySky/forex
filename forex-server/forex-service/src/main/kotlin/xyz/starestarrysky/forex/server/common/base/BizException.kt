package xyz.starestarrysky.forex.server.common.base

class BizException : RuntimeException {
    companion object {
        private const val serialVersionUID = 684938209066191753L

        const val ERROR_CODE = "9999"
        const val SUCCESS_CODE = "10000"

        fun builder(): BusExceptionBuilder {
            return BusExceptionBuilder()
        }

        class BusExceptionBuilder {
            private var message: String? = null
            private var code = "9999"
            private var httpStatus = 500

            fun message(message: String?): BusExceptionBuilder {
                this.message = message
                return this
            }

            fun code(code: String): BusExceptionBuilder {
                this.code = code
                return this
            }

            fun httpStatus(status: Int): BusExceptionBuilder {
                this.httpStatus = status
                return this
            }

            fun build(): BizException {
                return BizException(message, code, httpStatus)
            }
        }
    }

    var code: String = SUCCESS_CODE
    var httpStatus: Int = 500

    constructor(message: String?) : super(message) {
        this.code = ERROR_CODE
    }

    constructor(throwable: Throwable) : super(throwable) {
        this.code = ERROR_CODE
    }

    constructor(message: String?, throwable: Throwable) : super(message, throwable) {
        this.code = ERROR_CODE
    }

    constructor(message: String?, code: String) : super(message) {
        this.code = code
    }

    constructor(message: String?, code: String, throwable: Throwable) : super(message, throwable) {
        this.code = code
    }

    constructor(message: String?, code: String, httpStatus: Int) : super(message) {
        this.code = code
        this.httpStatus = httpStatus
    }
}
