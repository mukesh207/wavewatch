package com.wavewatch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavewatch.data.model.SecurityAlert
import com.wavewatch.data.repository.ChatbotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val chatbotRepository: ChatbotRepository
) : ViewModel() {

    private val _explanation = MutableStateFlow<String?>(null)
    val explanation: StateFlow<String?> = _explanation

    fun explainAlert(alert: SecurityAlert) {
        viewModelScope.launch {
            _explanation.value = null
            _explanation.value = chatbotRepository.explainThreat(
                alert.title,
                alert.description
            )
        }
    }
}
