package cf.vbnm.amoeba.core

import java.util.concurrent.atomic.AtomicInteger

class Statistics private constructor() {
    companion object {
        val Statistics = Statistics()
        private var totalSent = AtomicInteger(0)
        private var totalReceived = AtomicInteger(0)
        private var totalDropped = AtomicInteger(0)
        private var waitingEvents = AtomicInteger(0)
        private var loopEvents = AtomicInteger(0)

        fun getTotalSent(): Int {
            return totalSent.get()
        }

        fun getTotalReceived(): Int {
            return totalReceived.get()
        }

        fun getTotalDropped(): Int {
            return totalDropped.get()
        }

        fun getWaitingEvents(): Int {
            return waitingEvents.get()
        }

        fun getLoopEvents(): Int {
            return loopEvents.get()
        }

        fun addSent(): Int {
            return totalSent.incrementAndGet()
        }

        fun addReceived(): Int {
            return totalReceived.incrementAndGet()
        }

        fun addDropped(): Int {
            return totalDropped.incrementAndGet()
        }

        fun addWaitingEvents(): Int {
            return waitingEvents.incrementAndGet()
        }

        fun addLoopEvents(): Int {
            return loopEvents.incrementAndGet()
        }

        fun removeWaitingEvents(): Int {
            return waitingEvents.decrementAndGet()
        }

        fun removeLoopEvents(): Int {
            return loopEvents.decrementAndGet()
        }
    }
}