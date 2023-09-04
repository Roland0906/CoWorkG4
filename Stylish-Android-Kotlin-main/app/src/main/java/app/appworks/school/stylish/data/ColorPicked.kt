package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class ColorPicked(
    val cid: String,
    val memberId: String?,
    @Json(name = "recommend_color") val recommendColor: String,
): Parcelable