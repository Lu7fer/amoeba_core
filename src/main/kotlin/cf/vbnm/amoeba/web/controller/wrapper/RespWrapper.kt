package cf.vbnm.amoeba.web.controller.wrapper

import cf.vbnm.amoeba.constant.ResultCode
import java.time.LocalDateTime

class RespWrapper<T> private constructor(
    resultCode: ResultCode,
    val data: T,
    val message: String = resultCode.message
) {
    val code: Int
    val codeName: String
    var path: String? = null
    val timestamp = LocalDateTime.now()

    init {
        code = resultCode.code
        codeName = resultCode.name
    }

    companion object {
        fun build(resultCode: ResultCode): RespWrapper<Unit> {
            return RespWrapper(resultCode, Unit)
        }

        fun <T> build(resultCode: ResultCode, data: T): RespWrapper<T> {
            return RespWrapper(resultCode, data)
        }

        fun <T> ok(data: T): RespWrapper<T> {
            return build(ResultCode.SUCCESS, data)
        }

        fun <T> ok(): RespWrapper<Unit> {
            return build(ResultCode.SUCCESS)
        }

        fun error(e: Exception): RespWrapper<Exception> {
            return RespWrapper(
                ResultCode.INTERNAL_SERVER_ERROR,
                e,
                e.message ?: ResultCode.INTERNAL_SERVER_ERROR.message
            )
        }

        fun error(e: Exception, message: String): RespWrapper<Exception> {
            return RespWrapper(ResultCode.INTERNAL_SERVER_ERROR, e, message)
        }
    }
}