package app.appworks.school.stylish.coloranalysis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import app.appworks.school.stylish.R
import app.appworks.school.stylish.StylishApplication
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.source.StylishRepository

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

    val productSizesText: LiveData<String> = product.map {
        when (it.sizes.size) {
            0 -> ""
            1 -> it.sizes.first()
            else -> StylishApplication.instance.getString(R.string._dash_, it.sizes.first(), it.sizes.last())
        }
    }




}