package cf.vbnm.amoeba.entity.table.core

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "property")
open class Property() {
    constructor(name: String, value: String) : this() {
        this.name = name
        this.value = value
    }

    @Id
    @Column(name = "name", nullable = false, length = 128)
    open var name: String = ""

    @Column(name = "`value`", nullable = false, length = 256)
    open var value: String = ""

    override fun toString(): String {
        return "Property(name='$name', value='$value')"
    }

}