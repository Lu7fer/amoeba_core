package cf.vbnm.amoeba.util

import cf.vbnm.amoeba.constant.DateTimeFormat
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer

class LocalDateTimeSerializerFormatter private constructor() :
    LocalDateTimeSerializer(DateTimeFormat.LOCAL_DATE_TIME_FORMATTER) {
    companion object {
        val INSTANCE = LocalDateTimeSerializerFormatter()
    }
}