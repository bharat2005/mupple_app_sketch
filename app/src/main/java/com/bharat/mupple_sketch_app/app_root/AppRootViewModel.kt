package com.bharat.mupple_sketch_app.app_root

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

enum class AuthState{
    UNKNOWN,
    UNAUTHENTICATED,
    AUTHENTICATED
}

@HiltViewModel
class AppRootViewModel @Inject constructor(

) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState.UNKNOWN)
    val authState = _authState.asStateFlow()



}