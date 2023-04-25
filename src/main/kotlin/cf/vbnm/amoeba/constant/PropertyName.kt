package cf.vbnm.amoeba.constant

class PropertyName private constructor() {

    @Suppress("unused")
    companion object {
        const val SERVER_PREFIX = "server."
        const val SERVER_PORT = SERVER_PREFIX + "port"
        const val SERVER_ADDRESS = SERVER_PREFIX + "address"
        const val SERVER_CONTEXT_PATH = SERVER_PREFIX + "contextPath"
        const val SERVER_SERVLET_PATH = SERVER_PREFIX + "servletPath"

        const val SERVER_WS_PREFIX = SERVER_PREFIX + "ws."
        const val SERVER_WS_SERVLET_PATH = SERVER_WS_PREFIX + "servletPath"

        const val LOGGER_PREFIX = "logger."
        const val LOGGER_LEVEL = LOGGER_PREFIX + "level"
        const val LOGGER_OUTPUT = LOGGER_PREFIX + "output"

        fun defaultProperties(name: String): String? {
            return when (name) {
                SERVER_PORT -> "8089"
                SERVER_ADDRESS -> "0.0.0.0"
                SERVER_CONTEXT_PATH -> "/"
                SERVER_SERVLET_PATH -> "/"
                LOGGER_LEVEL -> "INFO"
                LOGGER_OUTPUT -> "System.out"
                SERVER_WS_SERVLET_PATH -> "/ws"
                else -> null
            }
        }
    }

}