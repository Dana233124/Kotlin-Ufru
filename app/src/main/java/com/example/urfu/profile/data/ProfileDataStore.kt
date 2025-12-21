package com.example.urfu.profile.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.urfu.profile.domain.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.profileDataStore by preferencesDataStore("profile_prefs")

class ProfileDataStore(private val context: Context) {

    private val KEY_NAME = stringPreferencesKey("name")
    private val KEY_RESUME = stringPreferencesKey("resume_url")
    private val KEY_AVATAR = stringPreferencesKey("avatar_uri")

    val profileFlow: Flow<Profile> =
        context.profileDataStore.data.map { prefs ->
            Profile(
                name = prefs[KEY_NAME] ?: "",
                resumeUrl = prefs[KEY_RESUME] ?: "",
                avatarUri = prefs[KEY_AVATAR] ?: ""
            )
        }

    suspend fun saveProfile(profile: Profile) {
        context.profileDataStore.edit { prefs ->
            prefs[KEY_NAME] = profile.name
            prefs[KEY_RESUME] = profile.resumeUrl
            prefs[KEY_AVATAR] = profile.avatarUri
        }
    }
}
