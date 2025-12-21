package com.example.urfu.profile.domain

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Profile>
    suspend fun saveProfile(profile: Profile)
}
