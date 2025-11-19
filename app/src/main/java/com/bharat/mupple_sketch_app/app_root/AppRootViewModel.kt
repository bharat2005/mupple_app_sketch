package com.bharat.mupple_sketch_app.app_root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharat.mupple_sketch_app.core.data.repo.AuthState
import com.bharat.mupple_sketch_app.core.domain.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject




@HiltViewModel
class AppRootViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    private val authRepository: AuthRepository
) : ViewModel() {

    val authState = authRepository.getAuthState().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AuthState.UNKNOWN
    )

    val isOnline = networkMonitor.isOnline


}