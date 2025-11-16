package com.bharat.mupple_sketch_app.auth_feature.presentation.navigation.start

import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(

 ) : ViewModel() {

    fun onGoogleSignIn(credential: AuthCredential){

    }



}