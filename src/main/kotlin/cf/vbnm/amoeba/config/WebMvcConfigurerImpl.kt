package cf.vbnm.amoeba.config

import jakarta.servlet.ServletContext
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.WebApplicationInitializer
import org.springframework.web.accept.HeaderContentNegotiationStrategy
import org.springframework.web.servlet.config.annotation.*

@Component
@EnableWebMvc
class WebMvcConfigurerImpl : WebMvcConfigurer, WebApplicationInitializer {


    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/")
            .setCachePeriod(3600)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowCredentials(false)
            .allowedMethods("*")
            .allowedOrigins("*")
    }

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer.defaultContentTypeStrategy(HeaderContentNegotiationStrategy())
        configurer.defaultContentType(MediaType.APPLICATION_JSON)
    }

    override fun onStartup(servletContext: ServletContext) {
        servletContext.sessionTimeout = 30 * 60
    }

}