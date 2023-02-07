package cf.vbnm.amoeba.web.event

import cf.vbnm.amoeba.core.log.Slf4kt.Companion.log
import org.springframework.beans.factory.getBeansWithAnnotation
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import java.lang.reflect.Method

interface EventFilter<E : Event<*>> {

    fun <R> filtering(event: E, block: (E) -> R): R?

    fun init()

}


inline fun <reified A : Annotation> EventFilter<*>.findAnnotatedMethods(applicationContext: ApplicationContext): MutableList<Pair<Method, Any>> {
    val annotatedMethodList = ArrayList<Pair<Method, Any>>()
    val withAnnotation = applicationContext.getBeansWithAnnotation<Component>().toMutableMap()
    withAnnotation += applicationContext.getBeansWithAnnotation<Controller>()
    withAnnotation += applicationContext.getBeansWithAnnotation<Configuration>()
    withAnnotation += applicationContext.getBeansWithAnnotation<Service>()
    withAnnotation.forEach { (_, u) ->
        u.javaClass.methods.forEach {
            val anno = AnnotationUtils.findAnnotation(it, A::class.java)
            if (anno != null) {
                log.trace("Add annotated method: {}", it)
                annotatedMethodList.add(Pair(it, u))
            }
        }
    }
    log.info(" Find methods with {}: {}", A::class.java.name, annotatedMethodList)
    return annotatedMethodList
}


interface Event<E> {
    fun get(): E
}