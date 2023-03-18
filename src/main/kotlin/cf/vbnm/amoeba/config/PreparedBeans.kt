package cf.vbnm.amoeba.config

import org.quartz.Scheduler
import org.quartz.impl.StdSchedulerFactory
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

    @Bean
    open fun getScheduler(): FactoryBean<Scheduler> {
        return object : FactoryBean<Scheduler> {
            override fun getObject(): Scheduler? {
                return StdSchedulerFactory.getDefaultScheduler()
            }

            override fun getObjectType(): Class<*> {
                return Scheduler::class.java
            }

        }
    }
}
