package cf.vbnm.amoeba.core.starter

import cf.vbnm.amoeba.annotation.PropertyChangeCheck
import cf.vbnm.amoeba.constant.PropertyName
import cf.vbnm.amoeba.constant.PropertyName.Companion.LOGGER_LEVEL
import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.log.LoggerImpl
import cf.vbnm.amoeba.core.log.LoggerImpl.Companion.defaultLogStream
import cf.vbnm.amoeba.core.log.LoggerImpl.Companion.setLoggerOutputFile
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.core.spi.Starter
import org.slf4j.event.Level
import org.springframework.stereotype.Component
import org.springframework.util.ResourceUtils
import java.io.PrintStream

private val log = Slf4kt.getLogger(LoggerConfigurationStarter::class.java)

@Component
class LoggerConfigurationStarter : Starter<LoggerConfigurationStarter> {

    private fun init(coreProperty: CoreProperty) {
        val level = coreProperty[LOGGER_LEVEL]
        log.trace("Get property 'logger.level': {}", level)
        try {
            setLogLevel(Level.valueOf(level))
        } catch (e: Exception) {
            setLogLevel(Level.INFO)
            log.warn("Logger level parse error", e)
        }
        log.info("Log level set to {} level", getLogLevel())
        val output = coreProperty["logger.output"]
        if (output == "System.out") {
            setPrintStream(System.out)
            return
        }
        val file = ResourceUtils.getFile(output)
        setLoggerOutputFile(file)
    }

    fun setLogLevel(level: Level) {
        log.trace("Logger level changed")
        LoggerImpl.level = level
    }

    fun getLogLevel(): Level {
        return LoggerImpl.level
    }

    fun setPrintStream(printStream: PrintStream) {
        log.trace("Logger output has be set")
        LoggerImpl.setLogStream(printStream)
    }


    override fun initProperty(coreProperty: CoreProperty) {
        init(coreProperty)
    }

    override fun getObject(): LoggerConfigurationStarter {
        return this
    }

    override fun getObjectType(): Class<*> {
        return LoggerConfigurationStarter::class.java
    }

    @PropertyChangeCheck(PropertyName.LOGGER_OUTPUT)
    fun loggerOutputChangeCheck(value: String): Boolean {
        log.debug("Event filter executed: loggerOutputChangeCheck({})", value)
        if (value == "System.out") {
            setPrintStream(defaultLogStream)
            return true
        }
        val file = ResourceUtils.getFile(value)
        return setLoggerOutputFile(file)
    }

}