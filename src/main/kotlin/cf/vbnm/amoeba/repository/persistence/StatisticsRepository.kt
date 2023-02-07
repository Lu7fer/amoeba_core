package cf.vbnm.amoeba.repository.persistence

import cf.vbnm.amoeba.entity.table.core.Statistics
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StatisticsRepository : JpaRepository<Statistics, String>