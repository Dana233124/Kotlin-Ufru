package com.example.urfu.profile.data

import com.example.urfu.profile.domain.Profile
import com.example.urfu.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val dataStore: ProfileDataStore
) : ProfileRepository {

    override fun getProfile(): Flow<Profile> = dataStore.profileFlow

    override suspend fun saveProfile(profile: Profile) {
        dataStore.saveProfile(profile)
    }
}
