package cf.vbnm.amoeba.annotation

import java.lang.annotation.Inherited

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Inherited
annotation class JobIdentity(val name: String, val group: String = "")
