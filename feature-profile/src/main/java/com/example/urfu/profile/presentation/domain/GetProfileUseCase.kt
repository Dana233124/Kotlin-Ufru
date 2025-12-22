package com.example.urfu.profile.presentation.domain


import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<Profile> = repository.getProfile()
}
