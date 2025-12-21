package com.example.urfu.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urfu.profile.domain.Profile
import com.example.urfu.profile.domain.ProfileRepository


import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repo: ProfileRepository
) : ViewModel() {

    fun save(name: String, resume: String, avatar: String, onDone: () -> Unit) {
        viewModelScope.launch {
            val profile = Profile(
                name = name,
                resumeUrl = resume,
                avatarUri = avatar
            )
            repo.saveProfile(profile)
            onDone()
        }
    }
}
