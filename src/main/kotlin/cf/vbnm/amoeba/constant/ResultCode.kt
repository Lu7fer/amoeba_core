package cf.vbnm.amoeba.constant

enum class ResultCode constructor(val code: Int, val message: String) {
    SUCCESS(200, "OK"),
    INTERNAL_SERVER_ERROR(500, "Internal server error")
}