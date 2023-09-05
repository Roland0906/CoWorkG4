package app.appworks.school.stylish.coloranalysis

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.R
import app.appworks.school.stylish.StylishApplication
import app.appworks.school.stylish.data.Color
import app.appworks.school.stylish.data.ColorPickerResult
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.Variant
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.login.UserManager
import app.appworks.school.stylish.network.StylishApiService
import app.appworks.school.stylish.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONArray

class ColorAnalysisViewModel (
    private val stylishRepository: StylishRepository,
    private val arguments: Product
) : ViewModel() {

    // ColorAnalysis has product data from arguments
    private val _product = MutableLiveData<Product>().apply {
        value = arguments
    }

    val product: LiveData<Product>
        get() = _product


    var selectedColorPosition = MutableLiveData<Int>()
    var selectedColorPosition2 = MutableLiveData<Int>()
    var selectedColorPosition3 = MutableLiveData<Int>()

    val selectedColor = MutableLiveData<Color>()
    val selectedColor2 = MutableLiveData<Color>()
    val selectedColor3 = MutableLiveData<Color>()

    var selectedVariantPosition = MutableLiveData<Int>()

    var selectedVariant = MutableLiveData<Variant>()

    val variantsBySelectedColor: LiveData<List<Variant>?> = selectedColor3.map { color ->
        color?.let {
            product.value?.variants?.filter { variant ->
                variant.colorCode == it.code
            }
        }
    }

    val amount = MutableLiveData<Long>()

    private val _navigateToAddedSuccess = MutableLiveData<Product>()

    val navigateToAddedSuccess: LiveData<Product>
        get() = _navigateToAddedSuccess

    // Handle navigation to Added Fail
    private val _navigateToAddedFail = MutableLiveData<Product>()

    val navigateToAddedFail: LiveData<Product>
        get() = _navigateToAddedFail

    // Handle leave add2cart
    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun insertToCart() {
        tracking("click","color_cart_adding")
        product.value?.let {
            coroutineScope.launch {
                selectedVariant.value?.apply {
                    it.selectedVariant = this
                    it.amount = amount.value
                    if (stylishRepository.isProductInCart(it.id, it.selectedVariant.colorCode, it.selectedVariant.size)) {

                        _navigateToAddedFail.value = it
                    } else {
                        stylishRepository.insertProductInCart(it)
                        _navigateToAddedSuccess.value = it
                    }
                }
            }
        }
    }
    fun tracking(type: String, event_value: String) {
        // memberId -> get its unique ID saved when user first signed up
        viewModelScope.launch {
            try{
                stylishRepository.trackUser(
                    UserManager.contentType,
                    StylishApiService.TrackUserBody(
                        UserManager.cid,
                        UserManager.member_id,
                        "Android",
                        UserManager.getDate(),
                        UserManager.getTimeStamp(),
                        type,
                        event_value,
                        UserManager.split_testing
                    )
                )}
            catch(e: Exception){
                Log.i("testAPI","trackUser failed")
            }
        }
    }




    val productSizesText: LiveData<String> = product.map {
        when (it.sizes.size) {
            0 -> ""
            1 -> it.sizes.first()
            else -> StylishApplication.instance.getString(R.string._dash_, it.sizes.first(), it.sizes.last())
        }
    }

    val decoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.left = 0
            } else {
                outRect.left = StylishApplication.instance.resources.getDimensionPixelSize(R.dimen.space_detail_circle)
            }
        }
    }

    val brown = Color("brown", "684F44")
    val black = Color("black", "3F3A3A")
    val darkRed = Color("darkRed", "902B0B")
    val hairColors = listOf(brown, black, darkRed)


    val skin = Color("skin", "F9DDB3")
    val orange = Color("orange", "FAD1B8")
    val milkTea = Color("milkTea","E4AE86")
    val skinColors = listOf(skin, orange, milkTea)


    val colorsInString = product.value?.colors?.map { it.code }
//    val colorsInArray = colorsInString?.toTypedArray()
    val jsonFormattedString = colorsInString?.joinToString(",", "[", "]") { "\"$it\""}

    fun postUserHairSkin(): ColorPickerResult {
        viewModelScope.launch {

            product.value?.let {

            stylishRepository.colorPicker(UserManager.cid, "", UserManager.getDate(), UserManager.getTimeStamp(), selectedColor.value!!.code, selectedColor2.value!!.code, jsonFormattedString)
        }}
        Log.i("API Testing", ColorPickerResult().toString())
        return ColorPickerResult()
    }

    val lightBlue = Color("lightBlue", "DDF0FF")
    var bestColorFromApi:List<Color?> = listOf(lightBlue)
    fun getBestColor(colors: List<Color?>) {
        bestColorFromApi = colors
    }



    fun onAddedSuccessNavigated() {
        _navigateToAddedSuccess.value = null
    }

    fun onAddedFailNavigated() {
        _navigateToAddedFail.value = null
    }


    fun selectColor(color: Color, position: Int) {
        Logger.w("selectColor=$color, position=$position")
        selectedVariantPosition.value = null
        selectedVariant.value = null
        selectedColor.value = color
        selectedColorPosition.value = position
    }

    fun selectColor2(color: Color, position: Int) {
        Logger.w("selectColor=$color, position=$position")
        selectedVariantPosition.value = null
        selectedVariant.value = null
        selectedColor2.value = color
        selectedColorPosition2.value = position

        Log.i("API Testing5", selectedColor.value!!.code)
        Log.i("API Testing6", selectedColor2.value!!.code)
    }

    fun selectColor3(color: Color, position: Int) {
        Logger.w("selectColor=$color, position=$position")
        selectedVariantPosition.value = null
        selectedVariant.value = null
        selectedColor3.value = color
        selectedColorPosition3.value = position
    }



    fun selectSize(variant: Variant, position: Int) {
        Logger.w("selectSize=$variant, position=$position")
        amount.value = 1
        selectedVariant.value = variant
        selectedVariantPosition.value = position
    }

    fun increaseAmount() {
        Logger.w("increaseAmount()")
        amount.value = amount.value?.plus(1)
    }

    fun decreaseAmount() {
        Logger.w("decreaseAmount()")
        amount.value = amount.value?.minus(1)
    }

    @InverseMethod("convertLongToString")
    fun convertStringToLong(value: String): Long {
        return try {
            value.toLong().let {
                when (it) {
                    0L -> 1
                    else -> it
                }
            }
        } catch (e: NumberFormatException) {
            1
        }
    }

    fun convertLongToString(value: Long): String {
        return value.toString()
    }

    fun leave() {
        _leave.value = true
    }

    fun onLeaveCompleted() {
        _leave.value = null
    }


    fun nothing() {}

}