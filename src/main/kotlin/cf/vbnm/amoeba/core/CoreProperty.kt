package cf.vbnm.amoeba.core

import cf.vbnm.amoeba.constant.PropertyName
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.core.log.Slf4kt.Companion.log
import cf.vbnm.amoeba.repository.persistence.PropertyRepository
import org.springframework.stereotype.Service

@Slf4kt
@Service
open class CoreProperty(
    private val propertyRepository: PropertyRepository,
) {

    operator fun get(name: String): String {
        val result = propertyRepository.findById(name)
        if (result.isPresent) {
            return result.get().value
        }
        return PropertyName.defaultProperties(name).apply { log.info("Get property: '{}': {}", name, this) }
    }

}