package cf.vbnm.amoeba.core

import cf.vbnm.amoeba.constant.PropertyName
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.entity.table.core.Property
import cf.vbnm.amoeba.repository.persistence.PropertyRepository
import org.springframework.stereotype.Service

private val log = Slf4kt.getLogger(CoreProperty::class.java)

@Service
open class CoreProperty(
    private val propertyRepository: PropertyRepository,
) {

    operator fun get(name: String): String {
        val result = propertyRepository.findById(name)
        if (result.isPresent) {
            return result.get().value
        }
        return PropertyName.defaultProperties(name).also { log.info("Get property: '{}': {}", name, this) }
    }

    /**
     * 使用Ant Pattern 匹配, 支持 * 和 ? . * 匹配任意数量字符, ? 匹配任意一个字符, * 和 ? 不能在开头, 如果在开头无法使用索引
     * */
    fun getWildcardProperty(name: String): List<Property> {
        return propertyRepository.findAllByNameLike(formatSqlLikePattern(name))
    }

    private fun formatSqlLikePattern(name: String): String {
        var pattern = name
        while (pattern.startsWith('*') || pattern.startsWith('?')) {
            pattern = pattern.removePrefix("*").removePrefix("?")
        }
        pattern = pattern.replace('*', '%').replace('?', '_')
        return pattern
    }
}