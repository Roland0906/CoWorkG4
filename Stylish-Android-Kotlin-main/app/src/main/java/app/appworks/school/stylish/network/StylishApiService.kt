package app.appworks.school.stylish.network

import android.os.Parcelable
import app.appworks.school.stylish.BuildConfig
import app.appworks.school.stylish.data.*
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import kotlinx.parcelize.Parcelize
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.Date

/**
 * Created by Wayne Chen in Jul. 2019.
 */
private const val HOST_NAME = "api.appworks-school.tw"
private const val HOST_NAME2 = "3.113.149.66:8000"
private const val API_VERSION = "1.0"
private const val BASE_URL = "https://$HOST_NAME/api/$API_VERSION/"
private const val BASE_URL2 = "http://$HOST_NAME2/api/$API_VERSION/"




/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */



internal val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

private val client = OkHttpClient.Builder()
    .addInterceptor(
        HttpLoggingInterceptor().apply {
            level = when (BuildConfig.LOGGER_VISIABLE) {
                true -> HttpLoggingInterceptor.Level.BODY
                false -> HttpLoggingInterceptor.Level.NONE
            }
        }
    )
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

private val retrofit2 = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL2)
    .client(client)
    .build()




/**
 * A public interface that exposes the [getMarketingHots], [getProductList], [getUserProfile],
 * [userSignIn], [checkoutOrder] methods
 */
interface StylishApiService {
    /**
     * Returns a Coroutine [Deferred] [MarketingHotsResult] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "marketing/hots" endpoint will be requested with the GET HTTP method
     */
    @GET("products/style?style=a") // not working, original -> "marketing/hots
    suspend fun getMarketingHots(): MarketingHotsResult

    /**
     * Returns a Coroutine [Deferred] [ProductListResult] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "products/{catalogType}" endpoint will be requested with the GET
     * HTTP method (catalogType: men, women, accessories)
     * The @Query annotation indicates that it will be added "?paging={pagingKey}" after endpoint
     */
    @GET("products/{catalogType}")
    suspend fun getProductList(
        @Path("catalogType") type: String,
        @Query("paging") paging: String? = null
    ): ProductListResult

    /**
     * Returns a Coroutine [Deferred] [UserProfileResult] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "user/profile" endpoint will be requested with the GET HTTP method
     * The @Header annotation indicates that it will be added "Authorization" header
     */
    @GET("profile") // "user/profile"
    suspend fun getUserProfile(@Header("Authorization") token: String): UserProfileResult
    /**
     * Returns a Coroutine [Deferred] [UserSignInResult] which can be fetched with await() if in a Coroutine scope.
     * The @POST annotation indicates that the "user/signin" endpoint will be requested with the POST HTTP method
     * The @Field annotation indicates that it will be added "provider", "access_token" key-pairs to the body of
     * the POST HTTP method, and it have to use @FormUrlEncoded to support @Field
     */
    @FormUrlEncoded
    @POST("user/signin")
    suspend fun userSignIn(
        @Field("provider") provider: String = "facebook",
        @Field("access_token") fbToken: String
    ): UserSignInResult


    @POST("signin") //"user/signin"
    suspend fun userSignIn(
        @Header("Content-Type") type: String = "application/json",
        @Body nativeSignInBody: NativeSignInBody
    ): UserSignIn?

    @POST("signup")
    suspend fun userSignUp(
        @Body nativeSignUpBody: NativeSignUpBody
    ): UserSignUpResult

    /**
     * Returns a Coroutine [Deferred] [CheckoutOrderResult] which can be fetched with await() if in a Coroutine scope.
     * The @POST annotation indicates that the "user/signin" endpoint will be requested with the POST HTTP method
     * The @Header annotation indicates that it will be added "Authorization" header
     * The @Body annotation indicates that it will be added [OrderDetail] to the body of the POST HTTP method
     */
    @POST("order/checkout")
    suspend fun checkoutOrder(
        @Header("Content-Type") type: String = "application/json",
        @Header("Authorization") token: String,
        @Body orderDetail: OrderDetail
    ): CheckoutOrderResult





    // Dong -> a bunch of body -> put header on top
    // a bunch of body -> need data class
    // Fields -> no need data class




    @POST("tracking")
    suspend fun trackUser(
        @Header("Content-type") contentType: String = "application/json",
        @Body trackUserBody: TrackUserBody
    )

    // Dong -> a bunch of body -> put header on top
    // a bunch of body -> need data class
    // Fields -> no need data class
    @Parcelize
    data class TrackUserBody(
        val cid: String,
        @Json(name = "member_id")val memberId: Int?,
        @Json(name = "device_os")val deviceOs: String,
        @Json(name = "event_date")val eventDate: String,
        @Json(name = "event_timestamp")val eventTimestamp: Int,
        @Json(name = "event_type")val eventType: String,
        @Json(name = "event_value")val eventValue: String,
        @Json(name = "split_testing")val splitTesting: String
    ) : Parcelable



    @POST("color_picker")
    suspend fun colorPicker(@Body request: ColorPickerRequest): ColorPickerResult


}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object StylishApi {
    val retrofitService: StylishApiService by lazy { retrofit.create(StylishApiService::class.java) }
    val retrofitService2: StylishApiService by lazy { retrofit2.create(StylishApiService::class.java) }
}


