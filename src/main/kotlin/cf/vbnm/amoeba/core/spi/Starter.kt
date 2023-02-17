package cf.vbnm.amoeba.core.spi

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.log.Slf4kt.Companion.log
import org.springframework.beans.factory.FactoryBean
import java.util.*

interface Starter<T> : FactoryBean<T> {
    companion object {
        fun findServices(): MutableList<Starter<*>> {
            log.trace("Preparing load services.")
            val loader = ServiceLoader.load(Starter::class.java)
            return loader.toMutableList()
        }
    }

    fun initProperty(coreProperty: CoreProperty)


}