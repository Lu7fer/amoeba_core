package cf.vbnm.amoeba.entity.table.core

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "statistics")
class Statistics(
    @Id
    @Column(
        name = "unique_id",
        nullable = false,
        length = 64,
    )
    private var uniqueId: String,

    @Column(name = "total_received")
    var totalReceived: Int,

    @Column(name = "total_dropped")
    var totalDropped: Int,

    @Column(name = "total_sent")
    var totalSent: Int,

    @Column(name = "waiting_events")
    var waitingEvents: Int,

    @Column(name = "looping_events")
    var loopingEvents: Int
) {
    constructor() : this("", 0, 0, 0, 0, 0)

    fun getUniqueId(): String {
        return uniqueId
    }

    fun setUniqueId(id: String) {
        this.uniqueId = id
    }

}