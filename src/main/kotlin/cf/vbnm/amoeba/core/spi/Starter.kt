package cf.vbnm.amoeba.core.spi

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.log.Slf4kt
import org.slf4j.Logger
import org.springframework.beans.factory.FactoryBean
import java.util.*

private val log: Logger
    get() = Slf4kt.getLogger(Starter::class.java)

interface Starter<T> : FactoryBean<T> {

    companion object {
        private lateinit var starters: MutableList<Starter<*>>

        fun findServices(): MutableList<Starter<*>> {
            if (this::starters.isInitialized)
                return starters
            log.trace("Preparing load services.")
            val loader = ServiceLoader.load(Starter::class.java)
            return loader.toMutableList()
        }
    }

    fun initProperty(coreProperty: CoreProperty)

    fun getEntityPackages(): Array<String> {
        return arrayOf()
    }

    fun getJpaConfigObject(): Class<*> {
        return Any::class.java
    }
}