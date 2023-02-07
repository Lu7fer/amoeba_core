package cf.vbnm.amoeba.config

import cf.vbnm.amoeba.util.LocalDateTimeSerializerFormatter
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.*
import org.springframework.http.MediaType
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import java.nio.charset.StandardCharsets


@Configuration
@ComponentScans(
    ComponentScan("cf.vbnm.amoeba.web")
)
@Import(WebMvcConfigurerImpl::class)
open class ServletConfiguration {
    @Bean
    open fun stringHttpMessageConverter(): StringHttpMessageConverter {
        return StringHttpMessageConverter(StandardCharsets.UTF_8)
    }

    @Bean
    open fun jackson2ObjectMapperFactoryBean(): Jackson2ObjectMapperFactoryBean {
        return Jackson2ObjectMapperFactoryBean().apply {
            this.setFailOnEmptyBeans(false)
            this.setSerializers(LocalDateTimeSerializerFormatter.INSTANCE)
        }
    }


    @Bean
    open fun jsonHttpMessageConverter(
        objectMapper: ObjectMapper
    ): MappingJackson2HttpMessageConverter {
        return MappingJackson2HttpMessageConverter(objectMapper).apply {
            val supportedMediaTypes: MutableList<MediaType> = ArrayList(this.supportedMediaTypes)
            MediaType::class.java.fields.forEach {
                if (it.type == MediaType::class.java) {
                    if (!it.isAnnotationPresent(java.lang.Deprecated::class.java)) {
                        val mediaType = it.get(null)
                        if (mediaType is MediaType) {
                            supportedMediaTypes.add(mediaType)
                        }
                    }
                }
            }
            this.supportedMediaTypes = supportedMediaTypes
        }
    }

    @Bean
    open fun requestMappingHandlerAdapter(converter: MappingJackson2HttpMessageConverter): RequestMappingHandlerAdapter {
        return RequestMappingHandlerAdapter().apply {
            messageConverters.add(converter)
        }
    }
}