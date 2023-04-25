package cf.vbnm.amoeba.config

import cf.vbnm.amoeba.core.log.Slf4kt
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaAuditing


private val log = Slf4kt.getLogger(CoreConfiguration::class.java)

@Configuration
@EnableJpaAuditing
@Import(PersistenceDatabaseConfiguration::class)
@ComponentScan("cf.vbnm.amoeba.core")
open class CoreConfiguration
