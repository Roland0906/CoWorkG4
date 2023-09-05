package app.appworks.school.stylish.data.source

import androidx.lifecycle.LiveData
import app.appworks.school.stylish.data.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.http.Body
import java.util.Date

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Concrete implementation to load Stylish sources.
 */
class DefaultStylishRepository(
    private val stylishRemoteDataSource: StylishDataSource,
    private val stylishLocalDataSource: StylishDataSource,
) : StylishRepository {

    override suspend fun getMarketingHots(): Result<List<HomeItem>> {
        return stylishRemoteDataSource.getMarketingHots()
    }

    override suspend fun getProductList(type: String, paging: String?): Result<ProductListResult> {
        return stylishRemoteDataSource.getProductList(type = type, paging = paging)
    }

    override suspend fun getUserProfile(token: String): Result<User> {
        return stylishRemoteDataSource.getUserProfile(token)
    }

    override suspend fun userSignIn(fbToken: String): Result<UserSignInResult> {
        return stylishRemoteDataSource.userSignIn(fbToken)
    }

    override suspend fun userSignIn(email: String, password: String): Result<UserSignInResult> {
        return stylishRemoteDataSource.userSignIn(email, password)
    }

    override suspend fun userSignUp(name: String?, email: String, password: String): Result<UserSignUpResult> {
        return stylishRemoteDataSource.userSignUp(name, email, password)
    }

    override suspend fun checkoutOrder(
        type: String,
        token: String,
        orderDetail: OrderDetail
    ): Result<CheckoutOrderResult> {
        return stylishRemoteDataSource.checkoutOrder(type, token, orderDetail)
    }

    override fun getProductsInCart(): LiveData<List<Product>> {
        return stylishLocalDataSource.getProductsInCart()
    }

    override suspend fun isProductInCart(id: Long, colorCode: String, size: String): Boolean {
        return stylishLocalDataSource.isProductInCart(id, colorCode, size)
    }

    override suspend fun insertProductInCart(product: Product) {
        stylishLocalDataSource.insertProductInCart(product)
    }

    override suspend fun updateProductInCart(product: Product) {
        stylishLocalDataSource.updateProductInCart(product)
    }

    override suspend fun removeProductInCart(id: Long, colorCode: String, size: String) {
        stylishLocalDataSource.removeProductInCart(id, colorCode, size)
    }

    override suspend fun clearProductInCart() {
        stylishLocalDataSource.clearProductInCart()
    }




//    override suspend fun colorPicker(
//        cid: String,
//        memberId: String?,
//        eventDate: String,
//        eventTimestamp: Int,
//        hair: String,
//        skin: String,
//        productColors: String?
//    ): ColorPickerResult {
//        return stylishRemoteDataSource.colorPicker(cid, memberId, eventDate, eventTimestamp, hair, skin, productColors)
//    }

    override suspend fun colorPicker(@Body request: ColorPickerRequest): ColorPickerResult {
        return stylishRemoteDataSource.colorPicker(request)
    }

}
