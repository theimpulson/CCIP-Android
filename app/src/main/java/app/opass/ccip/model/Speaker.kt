package app.opass.ccip.model

import android.content.Context
import android.os.Parcelable
import app.opass.ccip.util.LocaleUtil
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Speaker(
    val id: String,
    val avatar: String,
    val zh: Zh_,
    val en: En_
) : Parcelable {

    fun getSpeakerDetail(context: Context): SpeakerDetail {
        return if (LocaleUtil.getCurrentLocale(context).language == Locale("zh").language) {
            zh
        } else {
            en
        }
    }
}
