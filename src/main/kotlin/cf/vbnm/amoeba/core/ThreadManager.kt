package cf.vbnm.amoeba.core


import cf.vbnm.amoeba.core.log.Slf4kt
import java.util.*

private val log = Slf4kt.getLogger(ThreadManager::class.java)

class ThreadManager {
    companion object {
        private val threadList = LinkedList<Thread>()
        fun addThread(thread: Thread) {
            val iterator = threadList.iterator()
            while (iterator.hasNext()) {
                if (!iterator.next().isAlive) {
                    iterator.remove()
                }
            }
            threadList.add(thread)
        }

        init {
            Runtime.getRuntime().addShutdownHook(Thread { stopAll() })
        }

        private fun stopAll() {
            threadList.forEach {
                it.interrupt()
                log.warn("Thread: {} stopped", it.name)
            }
        }
    }


}