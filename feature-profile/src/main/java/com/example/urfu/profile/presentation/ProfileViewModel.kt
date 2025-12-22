package com.example.urfu.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urfu.profile.presentation.domain.GetProfileUseCase
import com.example.urfu.profile.presentation.domain.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    val profile = getProfileUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Profile()
        )
}
