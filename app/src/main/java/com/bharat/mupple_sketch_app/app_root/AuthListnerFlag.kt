package com.bharat.mupple_sketch_app.app_root

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Singleton

@Singleton
class AuthListnerFlag {
    private val _authListnerErrorState = MutableStateFlow<String?>(null)
    val authListenerState = _authListnerErrorState.asStateFlow()

    fun setAuthListnerState(error : String){
        _authListnerErrorState.update {  error }
    }
}