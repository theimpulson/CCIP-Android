package app.opass.ccip.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Zh(
    override val title: String,
    override val description: String
) : SessionDetail, Parcelable
