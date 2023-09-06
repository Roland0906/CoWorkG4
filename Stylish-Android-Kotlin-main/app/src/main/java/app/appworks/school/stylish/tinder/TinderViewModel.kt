package app.appworks.school.stylish.tinder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.login.UserManager
import app.appworks.school.stylish.network.StylishApi
import app.appworks.school.stylish.network.StylishApiService
import app.appworks.school.stylish.util.ServiceLocator.stylishRepository
import kotlinx.coroutines.launch

class TinderViewModel(private val stylishRepository: StylishRepository): ViewModel() {

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
                )
            }
            catch(e: Exception){
                Log.i("testAPI","trackUser failed")
            }
        }
    }
}