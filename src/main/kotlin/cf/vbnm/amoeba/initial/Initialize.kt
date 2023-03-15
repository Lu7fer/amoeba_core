package cf.vbnm.amoeba.initial

import cf.vbnm.amoeba.annotation.JobIdentity
import cf.vbnm.amoeba.core.log.Slf4kt
import org.quartz.Job
import org.quartz.JobBuilder
import org.quartz.Scheduler
import org.springframework.stereotype.Component

private val log = Slf4kt.getLogger(InitializeJobs::class.java)

@Component
class InitializeJobs(jobs: List<Job>, private val scheduler: Scheduler) {
    init {
        jobs.forEach { job ->
            scheduler.addJob(JobBuilder.newJob(job.javaClass).run {
                job::class.annotations.filterIsInstance<JobIdentity>().forEach {
                    withIdentity(it.name, it.group)
                }
                storeDurably()
                build()
            }, true)
            log.debug("add quartz job: {}", job.javaClass)
        }
        scheduler.start()
    }
}