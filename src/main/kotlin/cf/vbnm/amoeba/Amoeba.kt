package cf.vbnm.amoeba

import cf.vbnm.amoeba.core.CoreContext
import cf.vbnm.amoeba.core.ThreadManager
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.core.log.Slf4kt.Companion.log
import org.springframework.context.support.AbstractApplicationContext
import kotlin.system.exitProcess


@Slf4kt
class Amoeba {
    companion object {
        fun runApplication(): AbstractApplicationContext {
            try {
                log.info(
                    "\n{}",
                    "    ___                         __         \n"
                            + "   /   |  ____ ___  ____  ___  / /_  ____ _\n"
                            + "  / /| | / __ `__ \\/ __ \\/ _ \\/ __ \\/ __ `/\n"
                            + " / ___ |/ / / / / / /_/ /  __/ /_/ / /_/ / \n"
                            + "/_/  |_/_/ /_/ /_/\\____/\\___/_.___/\\__,_/  "
                )
                return CoreContext.getApplicationContext()
            } catch (e: Throwable) {
                ThreadManager.stopAll()
                e.printStackTrace()
                exitProcess(-1)
            }
        }
    }
}

fun main() {
    Amoeba.runApplication()
}
