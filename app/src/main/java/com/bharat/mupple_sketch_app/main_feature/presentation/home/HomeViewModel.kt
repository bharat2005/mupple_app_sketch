package com.bharat.mupple_sketch_app.main_feature.presentation.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject  constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    fun signOut(){
        firebaseAuth.signOut()

    }
}