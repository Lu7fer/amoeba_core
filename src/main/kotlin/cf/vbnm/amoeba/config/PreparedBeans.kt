package cf.vbnm.amoeba.config

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.quartz.Scheduler
import org.quartz.impl.DirectSchedulerFactory
import org.quartz.impl.SchedulerRepository
import org.quartz.simpl.RAMJobStore
import org.quartz.spi.ThreadPool
import org.springframework.beans.factory.FactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@Configuration
open class PreparedBeans {

    /**
     * 暂时使用
     * */
    @Bean
    open fun getRestTemplate(): RestTemplate {
        return RestTemplate()
    }

    companion object {
        private var isInitialized = false
    }

    @Bean
    open fun getScheduler(): FactoryBean<Scheduler> {
        return object : FactoryBean<Scheduler> {

            override fun getObject(): Scheduler {
                if (!isInitialized) {
                    val schedulerFactory = DirectSchedulerFactory.getInstance()
                    schedulerFactory.createScheduler("Amoeba", "NON_CLUSTERED", QuartzThreadPool(), RAMJobStore())
                    isInitialized = true
                }
                return SchedulerRepository.getInstance().lookup("Amoeba")
            }

            override fun getObjectType(): Class<*> {
                return Scheduler::class.java
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    class QuartzThreadPool : ThreadPool {
        override fun runInThread(runnable: Runnable): Boolean {
            GlobalScope.launch {
                runnable.run()
            }
            return true
        }

        override fun blockForAvailableThreads(): Int {
            return 10
        }

        override fun initialize() {
        }

        override fun shutdown(waitForJobsToComplete: Boolean) {

        }

        override fun getPoolSize(): Int {
            return 10
        }

        override fun setInstanceId(schedInstId: String) {
        }

        override fun setInstanceName(schedName: String) {
        }

    }
}
