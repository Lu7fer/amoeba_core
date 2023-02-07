package cf.vbnm.amoeba.core.starter

import cf.vbnm.amoeba.config.ServletConfiguration
import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.core.log.Slf4kt.Companion.log
import cf.vbnm.amoeba.core.spi.Starter
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ErrorPageErrorHandler
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component
import org.springframework.web.SpringServletContainerInitializer
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet
import java.net.InetSocketAddress


@Component
@Slf4kt
class WebServerStarter(private val applicationContext: AbstractApplicationContext) {
    private lateinit var server: Server
    private lateinit var webApplicationContext: AbstractApplicationContext


    fun getWebApplicationContext(): AbstractApplicationContext {
        return webApplicationContext
    }

    /**
     * 创建一个Jetty容器和相应的ApplicationContext并启动
     * */
    fun creatWebApplicationContext(coreProperty: CoreProperty): AbstractApplicationContext {
        val webApplicationContext = AnnotationConfigWebApplicationContext()
        webApplicationContext.parent = applicationContext
        server = Server(getInetSocketAddress(coreProperty))
        server.handler = ServletContextHandler().apply {
            errorHandler = ErrorPageErrorHandler()
            contextPath = coreProperty["server.contextPath"]
            addServletContainerInitializer(SpringServletContainerInitializer())
            this.addServlet(
                ServletHolder(DispatcherServlet(webApplicationContext)),
                coreProperty["server.servletPath"]
            )
            addEventListener(ContextLoaderListener(webApplicationContext))
//            resourceBase = ClassPathResource("/").uri.toString()
            webApplicationContext.servletContext = servletHandler.servletContext
        }
        webApplicationContext.register(ServletConfiguration::class.java)
        val providers = Starter.findServices()
        providers.forEach {
            webApplicationContext.register(it.javaClass)
            log.info("Registry starter: {}", it.javaClass.name)
            log.trace("Starting: {}", it)
            it.setProperty(coreProperty)
            log.info("Started: {}", it)
        }

        server.stopAtShutdown
        server.start()
        webApplicationContext.registerShutdownHook()
        webApplicationContext.refresh()
        log.info("Web server started...")
        this.webApplicationContext = webApplicationContext
        return webApplicationContext
    }

    private fun getInetSocketAddress(coreProperty: CoreProperty): InetSocketAddress {
        val address = coreProperty["server.address"]
        val port = coreProperty["server.port"].toInt()
        return InetSocketAddress.createUnresolved(address, port)
    }

}
