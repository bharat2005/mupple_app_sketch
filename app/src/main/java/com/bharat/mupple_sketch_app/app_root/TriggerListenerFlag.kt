package com.bharat.mupple_sketch_app.app_root

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TriggerListenerFlag @Inject constructor() {
    private val _triggerListenerState = MutableStateFlow(0)
    val triggerListenerState = _triggerListenerState.asStateFlow()


    fun trigger(){
        _triggerListenerState.value++
    }

}