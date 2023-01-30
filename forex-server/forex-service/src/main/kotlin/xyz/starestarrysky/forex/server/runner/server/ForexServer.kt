package xyz.starestarrysky.forex.server.runner.server

interface ForexServer {
    fun isRunning(): Boolean

    fun start()

    fun stop()
}
