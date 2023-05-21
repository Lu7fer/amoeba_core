package cf.vbnm.amoeba.config


import cf.vbnm.amoeba.core.spi.Starter
import org.h2.jdbcx.JdbcDataSource
import org.hibernate.dialect.H2Dialect
import org.hibernate.jpa.HibernatePersistenceProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.Database
import org.springframework.orm.jpa.vendor.HibernateJpaDialect
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.web.servlet.config.annotation.*
import java.io.File
import java.util.*
import javax.sql.DataSource


@Configuration
@ComponentScan("cf.vbnm.amoeba.repository.persistence")
@EnableJpaRepositories(
    "cf.vbnm.amoeba.repository.persistence",
    entityManagerFactoryRef = "persistenceEntityManagerFactory", transactionManagerRef = "persistenceTransactionManager"
)
open class PersistenceDatabaseConfiguration {

    companion object {
        private fun checkRunInIDEA(): Boolean {
            return try {
                Class.forName("com.intellij.rt.execution.application.AppMainV2")
                true
            } catch (_: ClassNotFoundException) {
                false
            }
        }
    }

    @Bean("persistenceDataSource")
    open fun persistenceDataSource(): DataSource {
        var file = File(this::class.java.protectionDomain.codeSource.location.file).parentFile
        file = if (checkRunInIDEA()) {
            File("D:\\DevelopmentWorkspace\\amoeba\\qdroid\\db")
        } else {
            File(file, "db")
        }
        file.mkdirs()
        if (!file.exists()) {
            throw Error("Create database folder failure")
        }
        val dbUrl = "jdbc:h2:file:${file.absolutePath.removeSuffix("/").removeSuffix("\\")}/core"
        val username = "amoeba"
        val password = "amoeba"
        return JdbcDataSource().apply {
            this.user = username
            this.password = password
            this.setUrl(dbUrl)
        }
    }

    @Bean("persistenceEntityManagerFactory")
    open fun entityManagerFactory(
        @Qualifier("persistenceDataSource") dataSource: DataSource,
        @Qualifier("persistenceJpaVendorAdapter") jpaVendorAdapter: HibernateJpaVendorAdapter
    ): LocalContainerEntityManagerFactoryBean {
        return LocalContainerEntityManagerFactoryBean().apply {
            this.dataSource = dataSource
            this.jpaVendorAdapter = jpaVendorAdapter
            val list = ArrayList<String>()
            list.add("cf.vbnm.amoeba.entity")
            Starter.findServices().forEach {
                list.addAll(it.getEntityPackages())
            }
            this.setPackagesToScan(*list.toTypedArray())
            persistenceProvider = HibernatePersistenceProvider()
            jpaDialect = HibernateJpaDialect()
        }
    }

    @Bean("persistenceJpaVendorAdapter")
    open fun jpaVendorAdapter(): HibernateJpaVendorAdapter {
        return HibernateJpaVendorAdapter().apply {
            this.setDatabase(Database.H2)
            this.setGenerateDdl(true)
            this.setShowSql(true)
            this.setDatabasePlatform(H2Dialect::class.java.name)
        }
    }

    @Bean("persistenceTransactionManager")
    open fun transactionManager(@Qualifier("persistenceEntityManagerFactory") entityManagerFactoryBean: LocalContainerEntityManagerFactoryBean): JpaTransactionManager {
        return JpaTransactionManager().apply {
            this.entityManagerFactory = entityManagerFactoryBean.`object`
        }
    }

}
