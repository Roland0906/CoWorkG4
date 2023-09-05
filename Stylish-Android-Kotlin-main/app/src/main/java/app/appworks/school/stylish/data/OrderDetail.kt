package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 * Created by Wayne Chen in Jul. 2019.
 */
//@Parcelize
//data class OrderDetail(
//    val prime: String,
//    val order: Order
//) : Parcelable



@Parcelize
data class Order(
    val shipping: String,
    val payment: String,
    val subtotal: Long,
    val freight: Long,
    val total: Long,
    val recipient: Recipient,
    val list: List<OrderProduct>
) : Parcelable

@Parcelize
data class Recipient(
    val name: String,
    val phone: String,
    val email: String,
    val address: String,
    val time: String
) : Parcelable

@Parcelize
data class OrderDetail(
//    val prime: String,
    val list: List<OrderProduct>
) : Parcelable

@Parcelize
data class OrderProduct(
    @Json(name = "product_id") val productId: Long,
    @Json(name = "product_name") val productName: String,
    @Json(name = "product_price") val productPrice: Int,
    val color: Color,
    @Json(name = "product_size") val productSize: String,
    @Json(name = "product_qty") val productQty: Long
) : Parcelable

//@Parcelize
//data class OrderProduct(
//    val id: Long,
//    val name: String,
//    val price: Int,
//    val color: Color,
//    val size: String,
//    val qty: Long
//) : Parcelable
