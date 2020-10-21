package sanchez.sanchez.sergio.androidmobiletest.persistence.network.serder

import android.annotation.SuppressLint
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class DateJsonAdapter {

    private val dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSS"

    private val dateFormatUTC0: SimpleDateFormat by lazy {
        SimpleDateFormat(dateFormat).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    @ToJson
    fun toJson(date: Date): String = dateFormatUTC0.format(date)

    /**
     * 5. No mostrar transacciones con formato de fecha inválido.
     *
     * La fechas con formato inválido generarán una excepción (java.text.ParseException) y la propiedad quedará a null
     */
    @FromJson
    fun fromJson(dateString: String): Date? = try {
        dateFormatUTC0.parse(dateString)
    } catch (ex: Exception) {
        null
    }
}