package ru.mmn.androidmaterialdesign.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mmn.androidmaterialdesign.BuildConfig
import ru.mmn.androidmaterialdesign.repository.PODRetrofitImpl
import ru.mmn.androidmaterialdesign.repository.PODServerResponseData

class PODViewModel(
    private val liveDataToObserve: MutableLiveData<PODData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) : ViewModel() {
    fun getLiveData(): LiveData<PODData> {
        return liveDataToObserve
    }

    fun sendServerRequest() {
        liveDataToObserve.postValue(PODData.Loading)
        val apiKey = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PODData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey).enqueue(
                object : Callback<PODServerResponseData> {
                    override fun onResponse(
                        call: Call<PODServerResponseData>,
                        response: Response<PODServerResponseData>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataToObserve.postValue(PODData.Success(response.body()!!))
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveDataToObserve.value =
                                    PODData.Error(Throwable("Unidentified error"))
                            } else {
                                liveDataToObserve.value =
                                    PODData.Error(
                                        Throwable(
                                            response.code().toString() + ":" + message
                                        )
                                    )
                            }
                        }
                    }

                    override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                        liveDataToObserve.value = PODData.Error(t)

                    }

                }
            )
        }
    }
}

