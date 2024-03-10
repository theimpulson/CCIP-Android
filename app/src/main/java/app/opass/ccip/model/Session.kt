package app.opass.ccip.model

import android.content.Context
import android.os.Parcelable
import app.opass.ccip.util.LocaleUtil
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Session(
    val id: String,
    val room: Room,
    val start: String,
    val end: String,
    val type: SessionType?,
    val uri: String?,
    val zh: Zh,
    val en: En,
    val speakers: List<Speaker>,
    val qa: String?,
    val slide: String?,
    val broadcast: List<String>?,
    val live: String?,
    val record: String?,
    val language: String?,
    @SerializedName("co_write")
    val coWrite: String?,
    val tags: List<SessionTag>
) : Parcelable {

    private val sdf: SimpleDateFormat get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault())
    val startLong: Long get() = sdf.parse(start)!!.time
    val endLong: Long get() = sdf.parse(end)!!.time

    fun getSessionDetail(context: Context): SessionDetail {
        return if (LocaleUtil.getCurrentLocale(context).language == Locale("zh").language) {
            zh
        } else {
            en
        }
    }
}
