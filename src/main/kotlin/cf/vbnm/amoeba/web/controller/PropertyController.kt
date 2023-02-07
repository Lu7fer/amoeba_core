package cf.vbnm.amoeba.web.controller

import cf.vbnm.amoeba.core.log.Slf4kt.Companion.log
import cf.vbnm.amoeba.web.service.PropertyService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/core/property", produces = [MediaType.APPLICATION_JSON_VALUE])
open class PropertyController(private val propertyService: PropertyService) {

    @RequestMapping("/test")
    fun test(): String {
        return "test"
    }

    @RequestMapping("/update/{name}")
    fun updateProperty(@PathVariable("name") name: String, value: String): Any? {
        log.info("Change property: '{}' = {}", name, value)
        return propertyService.set(name, value)
    }

    @RequestMapping("/delete/{name}")
    fun deleteProperty(@PathVariable("name") name: String): Any {
        log.info("Delete property: '{}'", name)
        return propertyService.delete(name)
    }

}