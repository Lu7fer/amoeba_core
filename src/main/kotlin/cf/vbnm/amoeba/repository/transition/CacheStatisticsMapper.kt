package cf.vbnm.amoeba.repository.transition

import cf.vbnm.amoeba.entity.table.core.Statistics
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CacheStatisticsMapper : JpaRepository<Statistics, String>