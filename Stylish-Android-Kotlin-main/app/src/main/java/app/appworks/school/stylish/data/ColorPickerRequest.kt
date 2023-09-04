package app.appworks.school.stylish.data

import com.squareup.moshi.Json

data class ColorPickerRequest(
    val cid: String = "",
    @Json(name = "member_id")val memberId: String? = "",
    @Json(name = "event_date")val eventDate: String,
    @Json(name = "event_timestamp")val eventTimestamp: Int = -1,
    val hair: String = "",
    val skin: String = "",
    val colors: List<String>?
)