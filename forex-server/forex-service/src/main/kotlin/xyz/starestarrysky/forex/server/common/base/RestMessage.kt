package xyz.starestarrysky.forex.server.common.base

class RestMessage(var code: String, var message: String) {
    companion object {
        val SUCCESS = RestMessage("10000", "成功")
        val UNKNOW_ERROR = RestMessage("9999", "未知错误")
    }
}
