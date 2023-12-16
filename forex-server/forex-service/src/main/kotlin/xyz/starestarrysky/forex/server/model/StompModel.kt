package xyz.starestarrysky.forex.server.model

import java.io.Serializable

class StompModel : Serializable {
    companion object {
        private const val serialVersionUID = 5867106684885628463L
    }

    lateinit var title: String

    lateinit var body: String
}
