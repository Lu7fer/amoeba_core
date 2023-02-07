package cf.vbnm.amoeba.core

import cf.vbnm.amoeba.config.CoreConfiguration
import cf.vbnm.amoeba.core.spi.Starter
import cf.vbnm.amoeba.core.starter.WebServerStarter
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.AbstractApplicationContext

class CoreContext {

    companion object {
        private val applicationContext: AbstractApplicationContext
        private val propertyContext: AbstractApplicationContext = AnnotationConfigApplicationContext().apply {
            this.register(CoreConfiguration::class.java)
            this.refresh()
        }

        fun getApplicationContext(): AbstractApplicationContext {
            return applicationContext
        }

        fun getCoreContext(): AbstractApplicationContext {
            return propertyContext
        }

        init {
            val starterMutableMap = propertyContext.getBeansOfType(Starter::class.java)
            starterMutableMap.forEach { (_, u) ->
                u.setProperty(propertyContext.getBean(CoreProperty::class.java))
            }
            applicationContext = propertyContext.getBean(WebServerStarter::class.java)
                .creatWebApplicationContext(propertyContext.getBean(CoreProperty::class.java))
        }

    }
}