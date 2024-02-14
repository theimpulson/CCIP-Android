package app.opass.ccip.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WifiNetworkInfo(
    @SerializedName("SSID")
    val ssid: String,
    @SerializedName("password")
    val password: String?
) : Parcelable
