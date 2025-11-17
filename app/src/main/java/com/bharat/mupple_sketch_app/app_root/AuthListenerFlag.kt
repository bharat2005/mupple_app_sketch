package com.bharat.mupple_sketch_app.app_root

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed class AuthEvents{
    object Success : AuthEvents()
    data class Error(val error : String) : AuthEvents()
}

@Singleton
class AuthListenerFlag @Inject constructor(

) {

    private val _authEvents = MutableSharedFlow<AuthEvents>()
    val authEvents = _authEvents.asSharedFlow()

    suspend fun onSuccess(){
        _authEvents.emit(AuthEvents.Success)
    }

    suspend fun onError(error : String){
        _authEvents.emit(AuthEvents.Error(error))
    }

}