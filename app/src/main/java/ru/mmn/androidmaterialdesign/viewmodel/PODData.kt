package ru.mmn.androidmaterialdesign.viewmodel

import ru.mmn.androidmaterialdesign.repository.PODServerResponseData

sealed class PODData {
    data class Success(val serverResponseData: PODServerResponseData) : PODData()
    data class Error(val error: Throwable) : PODData()
    object Loading : PODData()

}
