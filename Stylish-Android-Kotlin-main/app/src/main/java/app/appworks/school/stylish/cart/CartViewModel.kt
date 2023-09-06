package app.appworks.school.stylish.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.login.UserManager
import app.appworks.school.stylish.network.StylishApiService
import app.appworks.school.stylish.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [CartFragment].
 */

data class CartUiState(
    val onRemoveClick: (product: Product) -> Unit,
    val onIncreaseClick: (product: Product) -> Unit,
    val onDecreaseClick: (product: Product) -> Unit,
)
class CartViewModel(private val stylishRepository: StylishRepository) : ViewModel() {

    // Get products from database to provide count number to bottom badge of cart
    val products: LiveData<List<Product>> = stylishRepository.getProductsInCart()

    // Handle navigation to payment
    private val _navigateToPayment = MutableLiveData<Boolean>()

    val navigateToPayment: LiveData<Boolean>
        get() = _navigateToPayment

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val uiState = CartUiState(
        onRemoveClick = {
            removeProduct(it)
        },
        onIncreaseClick = {
            increaseAmount(it)
        },
        onDecreaseClick = {
            decreaseAmount(it)
        },
    )

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
    fun tracking(type: String) {
        // memberId -> get its unique ID saved when user first signed up
        viewModelScope.launch {
            try{
                stylishRepository.trackUser(
                    UserManager.contentType,
                    StylishApiService.TrackUserBody(
                        UserManager.cid,
                        UserManager.userIdFromApi,
                        "Android",
                        UserManager.getDate(),
                        UserManager.getTimeStamp(),
                        type,
                        "cart",
                        UserManager.split_testing
                    )
                )
            }
            catch(e: Exception){
                Log.i("testAPI","trackUser failed")
            }
        }
    }

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]$this")
        Logger.i("------------------------------------")
    }

    fun navigateToPayment() {
        _navigateToPayment.value = true
    }

    fun onPaymentNavigated() {
        _navigateToPayment.value = null
    }

    /**
     * Remove the given [Product] from Room database
     */
    fun removeProduct(product: Product) {
        coroutineScope.launch {
            stylishRepository.removeProductInCart(product.id, product.selectedVariant.colorCode, product.selectedVariant.size)
        }
    }

    /**
     * Update the given [Product] from Room database
     */
    private fun updateProduct(product: Product) {
        product.amount?.let { amount ->
            product.selectedVariant.let {
                if (amount in 1..it.stock) {
                    coroutineScope.launch {
                        stylishRepository.updateProductInCart(product)
                    }
                }
            }
        }
    }

    /**
     * Update the given [Product] with new amount
     */
    fun increaseAmount(product: Product) {
        product.amount = product.amount?.plus(1)
        updateProduct(product)
    }

    /**
     * Update the given [Product] with new amount
     */
    fun decreaseAmount(product: Product) {
        product.amount = product.amount?.minus(1)
        updateProduct(product)
    }
}
