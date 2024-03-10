package app.opass.ccip.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Zh_(
    override val name: String,
    override val bio: String
) : SpeakerDetail, Parcelable
