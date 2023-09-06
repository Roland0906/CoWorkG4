package app.appworks.school.stylish.data

import com.squareup.moshi.Json

data class BestSeller(
    val data:BestSellerData
)

data class BestSellerData(
    @Json(name = "product_id")val productId: Long,
    @Json(name = "product_title")val productTitle: String,
    val sold: Int,
    @Json(name = "time_range")val timeRange: Int
)