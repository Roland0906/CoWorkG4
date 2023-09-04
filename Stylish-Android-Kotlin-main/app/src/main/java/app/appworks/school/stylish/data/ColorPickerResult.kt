package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ColorPickerResult(
    @Json(name = "data") val data: ColorPicked? = null
) : Parcelable