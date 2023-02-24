package cf.vbnm.amoeba.web.service

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.entity.table.core.Property
import cf.vbnm.amoeba.repository.persistence.PropertyRepository
import cf.vbnm.amoeba.web.event.PropertyChangeEventBuilder
import cf.vbnm.amoeba.web.event.PropertyChangeEventFilter
import org.springframework.stereotype.Service

private val log = Slf4kt.getLogger(PropertyService::class.java)

@Service
class PropertyService(
    private val propertyRepository: PropertyRepository,
    private val propertyChangeEventFilter: PropertyChangeEventFilter,
    private val builder: PropertyChangeEventBuilder
) : CoreProperty(propertyRepository) {

    operator fun set(name: String, value: String): Property? {
        return propertyChangeEventFilter.filtering(builder.build(name, value)) {
            log.info("Save property: {}", it.get())
            propertyRepository.save(it.get())
        }
    }

    fun delete(name: String) {
        propertyRepository.deleteById(name)
    }
}