package cf.vbnm.amoeba.core

import cf.vbnm.amoeba.config.CoreConfiguration
import cf.vbnm.amoeba.core.starter.WebServerContextLoader
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
            applicationContext = propertyContext.getBean(WebServerContextLoader::class.java)
                .creatWebApplicationContext(propertyContext.getBean(CoreProperty::class.java))
        }

    }
}