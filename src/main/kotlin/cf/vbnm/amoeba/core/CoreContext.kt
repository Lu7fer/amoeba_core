package cf.vbnm.amoeba.core

import cf.vbnm.amoeba.config.CoreConfiguration
import cf.vbnm.amoeba.core.spi.Starter
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.AbstractApplicationContext

class CoreContext {

    companion object {
        private val webApplicationContext: AbstractApplicationContext
        private val jpaContext: AbstractApplicationContext = AnnotationConfigApplicationContext().apply {
            Starter.findServices().forEach {
                val clazz = it.getJpaConfigObject()
                if (clazz != Any::class.java)
                    this.register(clazz)
            }
            this.register(CoreConfiguration::class.java)
            this.refresh()
        }

        fun getApplicationContext(): AbstractApplicationContext {
            return webApplicationContext
        }

        fun getCoreContext(): AbstractApplicationContext {
            return jpaContext
        }

        init {
            webApplicationContext = jpaContext.getBean(WebServerContextLoader::class.java)
                .creatWebApplicationContext(jpaContext.getBean(CoreProperty::class.java))
        }

    }
}