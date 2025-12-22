package com.example.urfu.profile.domain

import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: Profile) {
        repository.saveProfile(profile)
    }
}
