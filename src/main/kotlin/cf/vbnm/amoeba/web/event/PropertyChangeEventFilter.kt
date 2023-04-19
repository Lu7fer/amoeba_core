package cf.vbnm.amoeba.web.event

import cf.vbnm.amoeba.annotation.PropertyChangeCheck
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.entity.table.core.Property
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.lang.reflect.Method

private val log = Slf4kt.getLogger(PropertyChangeEventFilter::class.java)

@Component
class PropertyChangeEventFilter(private val applicationContext: ApplicationContext) : EventFilter<PropertyChangeEvent> {
    private val annotatedMethods = HashMap<String, MutableList<Pair<Method, Any>>>()

    @Volatile
    private var isInit = false
    override fun init() {
        val annotatedMethods = findAnnotatedMethods<PropertyChangeCheck>(applicationContext)
        annotatedMethods.forEach {
            val annotation = it.first.getAnnotation(PropertyChangeCheck::class.java)
            val list = this.annotatedMethods[annotation.name]
            if (list == null) {
                this.annotatedMethods[annotation.name] = arrayListOf(it)
            } else {
                list.add(it)
            }

        }
        isInit = true
        this.annotatedMethods.forEach { (_, u) ->
            u.sortByDescending {
                it.first.getAnnotation(PropertyChangeCheck::class.java).order
            }
        }
        log.info("Methods: {}", this.annotatedMethods)
    }

    /**
     * 过滤器
     * @param event 事件
     * @param block 过滤器通过过滤后做的事
     * */
    override fun <R> filtering(event: PropertyChangeEvent, block: (PropertyChangeEvent) -> R): R? {
        if (!isInit) init()
        val list = annotatedMethods[event.name]
        var isPass = true
        var checkedValue = event.value
        list?.forEach {
            if (isPass) {
                val parameterTypes = it.first.parameterTypes
                if (parameterTypes.size == 1 && parameterTypes[0] == String::class.java) {
                    val invoked = it.first.invoke(it.second, checkedValue)
                    if (invoked is String) {
                        checkedValue = invoked
                    }
                    if (invoked is Boolean) {
                        isPass = invoked
                        if (!invoked) log.info("Filter breaking: {}: {}", event.name, event.value)
                    }
                }
            }
        }
        if (isPass) {
            log.info("Passed filter test: {}", event.name)
            event.value = checkedValue
            return block.invoke(event)
        }
        return null
    }
}

@Component
class PropertyChangeEventBuilder {

    fun build(name: String, value: String): PropertyChangeEvent {
        return PropertyChangeEvent(name, value)
    }

}

class PropertyChangeEvent(val name: String, var value: String) : Event<Property> {
    override fun get(): Property {
        return Property(name, value)
    }
}