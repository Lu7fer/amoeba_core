package cf.vbnm.amoeba.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
open class PreparedBeans {

    @Bean
    open fun getRestTemplate(): RestTemplate {
        return RestTemplate(OkHttp3ClientHttpRequestFactory())
    }

}