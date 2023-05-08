package cf.vbnm.amoeba.entity.table.core

import jakarta.persistence.*

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

    @Lob
    @Column(name = "`value`", nullable = false)
    open var value: String = ""

    override fun toString(): String {
        return "Property(name='$name', value='$value')"
    }

}