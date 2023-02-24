package cf.vbnm.amoeba.web.controller.aop

import cf.vbnm.amoeba.annotation.ResponseNotIntercept
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.web.controller.wrapper.RespWrapper
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

private val log = Slf4kt.getLogger(ResponseAdvice::class.java)


@RestControllerAdvice
class ResponseAdvice : ResponseBodyAdvice<Any> {

    @ExceptionHandler(Exception::class)
    fun restControllerRespWrap(exception: Exception, request: HttpServletRequest): RespWrapper<*> {
        log.warn("An exception occurred due servlet process", exception)
        return RespWrapper.error(exception).apply { path = request.requestURI }
    }

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        //若加了@ResponseNotIntercept 则该方法不用做统一的拦截
        val annotatedElement = returnType.parameterType
        val annotation = AnnotationUtils.findAnnotation(annotatedElement, ResponseNotIntercept::class.java)
        return annotation == null
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any {
        if (body is RespWrapper<*>) return body
        return RespWrapper.ok(body).apply { path = request.uri.path }
    }
}