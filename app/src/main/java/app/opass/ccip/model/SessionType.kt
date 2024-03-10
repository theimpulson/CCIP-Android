package app.opass.ccip.model

import android.content.Context
import android.os.Parcelable
import app.opass.ccip.util.LocaleUtil
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class SessionType(
    val id: String,
    val zh: Zh,
    val en: En
) : Parcelable {

    fun getDetails(context: Context) =
        if (LocaleUtil.getCurrentLocale(context).language == Locale("zh").language) zh else en

    interface LocalizedDetail {
        val name: String
    }

    @Parcelize
    data class Zh(override val name: String) : Room.LocalizedDetail, Parcelable

    @Parcelize
    data class En(override val name: String) : Room.LocalizedDetail, Parcelable
}
