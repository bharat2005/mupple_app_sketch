package com.bharat.mupple_sketch_app.personalization_feature.presentation.personalization

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bharat.mupple_sketch_app.personalization_feature.domain.PersonalizationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class PersonalizationUiState(
    val isLoading: Boolean = false,
    val personalizationError : String? = null,
    val personalizationSuccess : Boolean = false
)
@HiltViewModel
class PersonalizationViewModel @Inject constructor(
    private  val personalizationRepo: PersonalizationRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonalizationUiState())
    val  uiState = _uiState.asStateFlow()


    fun onDismissError(){
        _uiState.update { it.copy(personalizationError = null) }
    }


    fun onSubmitPersonalization() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            personalizationRepo.savePersonalizationDetails().collect { result ->
                result.fold(
                    onSuccess = { _uiState.update { it.copy(personalizationSuccess = true, personalizationError = null) }},
                    onFailure = {  e -> _uiState.update { it.copy(personalizationError = e.message, personalizationSuccess = false, isLoading = false) }}
                )
            }

        }

    }
}