package app.appworks.school.stylish.catalog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.login.UserManager
import app.appworks.school.stylish.network.StylishApiService
import kotlinx.coroutines.launch

class CatalogViewModel(private val stylishRepository: StylishRepository): ViewModel() {

    fun tracking(type: String, page: String) {
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
                        page,
                        UserManager.split_testing
                    )
                )
            }
            catch(e: Exception){
                Log.i("testAPI","trackUser failed")
            }
        }
    }
}