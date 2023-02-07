package cf.vbnm.amoeba.core

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.core.log.Slf4kt.Companion.log
import java.util.*

@Slf4kt
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

        fun stopAll() {
            threadList.forEach {
                it.interrupt()
                log.info("Thread: {} stopped", it.name)
            }
        }
    }


}