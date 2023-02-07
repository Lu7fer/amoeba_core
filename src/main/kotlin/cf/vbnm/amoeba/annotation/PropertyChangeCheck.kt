package cf.vbnm.amoeba.annotation

/**
 * 方法注解, 标记的方法在事件发出后执行, 方法参数为(value: String), 内容为属性指
 * @param name 属性名称
 * @param order 调用顺序, 默认100, 数字越大, 优先级越高
 * */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class PropertyChangeCheck(val name: String, val order: Int = 100)
