package cf.vbnm.amoeba.core.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Slf4kt {
    companion object {
        fun getLogger(clazz: Class<*>): Logger {
            return LoggerFactory.getLogger(clazz)
        }
    }
}


