package cf.vbnm.amoeba.core

import cf.vbnm.amoeba.config.PreparedBeans
import cf.vbnm.amoeba.config.ServletConfiguration
import cf.vbnm.amoeba.constant.PropertyName.Companion.SERVER_ADDRESS
import cf.vbnm.amoeba.constant.PropertyName.Companion.SERVER_CONTEXT_PATH
import cf.vbnm.amoeba.constant.PropertyName.Companion.SERVER_PORT
import cf.vbnm.amoeba.constant.PropertyName.Companion.SERVER_SERVLET_PATH
import cf.vbnm.amoeba.constant.PropertyName.Companion.SERVER_WS_SERVLET_PATH
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.core.spi.Starter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.servlet.ErrorPageErrorHandler
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.util.thread.ThreadPool
import org.eclipse.jetty.websocket.server.JettyWebSocketServlet
import org.eclipse.jetty.websocket.server.JettyWebSocketServletFactory
import org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component
import org.springframework.web.SpringServletContainerInitializer
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet
import java.net.InetSocketAddress

private val log = Slf4kt.getLogger(WebServerContextLoader::class.java)

@Component
class WebServerContextLoader(private val applicationContext: AbstractApplicationContext) {
    private lateinit var jettyServer: Server
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
        jettyServer = Server(JettyThreadPool())
        jettyServer.addConnector(ServerConnector(jettyServer).apply {
            val socketAddress = getInetSocketAddress(coreProperty)
            port = socketAddress.port
            host = socketAddress.hostName
        })
        jettyServer.handler = ServletContextHandler().apply {
            errorHandler = ErrorPageErrorHandler()
            contextPath = coreProperty[SERVER_CONTEXT_PATH]
            addServletContainerInitializer(SpringServletContainerInitializer())
            this.addServlet(
                ServletHolder(DispatcherServlet(webApplicationContext)),
                coreProperty[SERVER_SERVLET_PATH]
            )
            addEventListener(ContextLoaderListener(webApplicationContext))
//            resourceBase = ClassPathResource("/").uri.toString()
            webApplicationContext.servletContext = servletHandler.servletContext

            addServletContainerInitializer(JettyWebSocketServletContainerInitializer())
            this.addServlet(ServletHolder("websocket", WebSocketServlet()), coreProperty[SERVER_WS_SERVLET_PATH])
            JettyWebSocketServletContainerInitializer.configure(this, null)
        }

        webApplicationContext.register(ServletConfiguration::class.java, PreparedBeans::class.java)
        val providers = Starter.findServices()
        providers.forEach {
            webApplicationContext.register(it.javaClass)
            log.info("Registry starter: {}", it.javaClass.name)
            log.trace("Starting: {}", it)
            it.initProperty(coreProperty)
            log.info("Started: {}", it.javaClass.name)
        }

        jettyServer.stopAtShutdown = true
        jettyServer.start()
        webApplicationContext.registerShutdownHook()
        webApplicationContext.refresh()
        log.info("Web server started...")
        this.webApplicationContext = webApplicationContext
        return webApplicationContext
    }

    private fun getInetSocketAddress(coreProperty: CoreProperty): InetSocketAddress {
        val address = coreProperty[SERVER_ADDRESS]
        val port = coreProperty[SERVER_PORT].toInt()
        return InetSocketAddress.createUnresolved(address, port)
    }

    companion object {
        class WebSocketServlet : JettyWebSocketServlet() {
            private val coreProperty = CoreContext.getCoreContext().getBean(CoreProperty::class.java)
            override fun configure(factory: JettyWebSocketServletFactory) {
                factory.getMapping(coreProperty[SERVER_WS_SERVLET_PATH])
                factory.register(this.javaClass)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private class JettyThreadPool : ThreadPool {
        override fun execute(command: Runnable) {
            GlobalScope.launch {
                command.run()
            }
        }

        override fun join() {
            GlobalScope.cancel("JettyServerStop")
        }

        override fun getThreads(): Int {
            return 10
        }

        override fun getIdleThreads(): Int {
            return 10
        }

        override fun isLowOnThreads(): Boolean {
            return false
        }

    }
}
